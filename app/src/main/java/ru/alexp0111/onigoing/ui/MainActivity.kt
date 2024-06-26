package ru.alexp0111.onigoing.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.ActivityMainBinding
import ru.alexp0111.onigoing.di.components.ActivityComponent
import ru.alexp0111.onigoing.navigation.NavigationTabTags
import ru.alexp0111.onigoing.navigation.Screens
import ru.alexp0111.onigoing.ui.base.BackPressable
import ru.alexp0111.onigoing.ui.base.RootFragment
import ru.alexp0111.onigoing.ui.utils.collectOnLifecycle
import ru.alexp0111.onigoing.utils.ColorThemes
import javax.inject.Inject

interface MenuConfigurator {
    fun updateMenuStateIfNeeded()
}

const val IS_RECREATED = "IS_RECREATED"

class MainActivity : AppCompatActivity(), MenuConfigurator {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel

    private val currentFragment: Fragment?
        get() = supportFragmentManager.fragments.firstOrNull { it.isVisible }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
        resolveColorTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()
        subscribeOnViewModel()
    }

    private fun resolveColorTheme() {
        val theme = when (viewModel.getColorTheme()) {
            ColorThemes.GREEN -> R.style.GreenActivity
            ColorThemes.ORANGE -> R.style.OrangeActivity
            ColorThemes.BLUE -> R.style.BlueActivity
            ColorThemes.RED -> R.style.RedActivity
        }
        setTheme(theme)
    }

    override fun onBackPressed() {
        val currentFragmentBackPressedResult = (currentFragment as? BackPressable)?.onBackPressed()
        when {
            currentFragmentBackPressedResult == true -> return
            currentFragment?.tag == NavigationTabTags.TAG_LISTS -> super.onBackPressed()
            else -> {
                selectTab(NavigationTabTags.TAG_LISTS)
                binding.navigationView.selectedItemId = R.id.menu_item_lists
            }
        }
    }

    private fun setUpBottomNavigation() = binding.navigationView.apply {
        setOnItemSelectedListener { item ->
            getTabTag(item.itemId)?.let { tag -> selectTab(tag) }
            true
        }
        setOnItemReselectedListener { (currentFragment as? RootFragment)?.setFirstScreen() }
    }

    private fun subscribeOnViewModel() {
        updateMenuStateIfNeeded()
    }

    override fun updateMenuStateIfNeeded() {
        collectOnLifecycle(viewModel.loggedIn) { loggedIn ->
            val isRecreated = intent.getBooleanExtra(IS_RECREATED, false)
            if (isRecreated) return@collectOnLifecycle
            binding.navigationView.apply {
                menu.findItem(R.id.menu_item_lists).isVisible = loggedIn
                menu.findItem(R.id.menu_item_search).isVisible = loggedIn
                if (!loggedIn) {
                    if (selectedItemId == R.id.menu_item_lists) {
                        this@MainActivity.deleteTab(NavigationTabTags.TAG_LISTS)
                    }
                    this@MainActivity.selectTab(NavigationTabTags.TAG_PROFILE)
                    selectedItemId = R.id.menu_item_profile
                } else {
                    if (selectedItemId == R.id.menu_item_profile) {
                        this@MainActivity.deleteTab(NavigationTabTags.TAG_PROFILE)
                    }
                    this@MainActivity.selectTab(NavigationTabTags.TAG_LISTS)
                    selectedItemId = R.id.menu_item_lists
                }
            }
        }
    }

    private fun selectTab(tabTag: String) {
        val oldFragment = currentFragment
        val newFragment = supportFragmentManager.findFragmentByTag(tabTag)
        if (oldFragment != null && newFragment != null && oldFragment === newFragment) return
        else supportFragmentManager.beginTransaction().apply {
            if (newFragment == null) {
                val rootFragment = createRootFragment(tabTag)
                add(binding.layoutFragmentContainer.id, rootFragment, tabTag)
            }
            oldFragment?.let { hide(it) }
            newFragment?.let { show(it) }
        }.commitNow()
    }

    private fun getTabTag(tabId: Int): String? = when (tabId) {
        R.id.menu_item_search -> NavigationTabTags.TAG_SEARCH
        R.id.menu_item_lists -> NavigationTabTags.TAG_LISTS
        R.id.menu_item_profile -> NavigationTabTags.TAG_PROFILE
        else -> null
    }

    private fun createRootFragment(tabTag: String): Fragment {
        val rootScreen = Screens.RootScreen(tabTag)
        return rootScreen.createFragment(supportFragmentManager.fragmentFactory)
    }

    private fun deleteTab(tabTag: String) = with(supportFragmentManager) {
        fragments.remove(findFragmentByTag(tabTag))
    }

    private fun injectSelf() {
        ActivityComponent.from(this).inject(this)
    }
}