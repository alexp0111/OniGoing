package ru.alexp0111.onigoing.ui.anime

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import net.nightwhistler.htmlspanner.HtmlSpanner
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentAnimeBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import ru.alexp0111.onigoing.ui.base.BackPressable
import ru.alexp0111.onigoing.ui.lists.page.Pages
import ru.alexp0111.onigoing.ui.lists.page.adapters.INCORRECT_SERIES_ET_INPUT_CODE
import ru.alexp0111.onigoing.ui.utils.subscribe
import ru.alexp0111.onigoing.utils.snack
import javax.inject.Inject

private const val TAG = "AnimeFragment"
private const val ANIME_ID_KEY = "ANIME_ID_KEY"
private const val ROUTER_TAG = "ROUTER_TAG_KEY"

// todo: 1. loading
//       2. Few images in preview
//       3. Normal episodes & airingTime check
//       4. Current episode logic

class AnimeFragment : Fragment(), BackPressable {

    @Inject
    lateinit var animeViewModelFactory: AnimeViewModelFactory
    lateinit var animeViewModel: AnimeViewModel

    private lateinit var binding: FragmentAnimeBinding

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val animeId = arguments?.getInt(ANIME_ID_KEY) ?: return
            if (animeViewModel.state.value.isUserDBResponseSuccess) {
                animeViewModel.updateStatusForAnime(
                    animeId = animeId,
                    status = Pages.from(position).ordinal,
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
        initViewModel()
        loadInfo()
    }

    private fun initViewModel() {
        val routerTag = arguments?.getString(ROUTER_TAG)
        animeViewModel = animeViewModelFactory.create(routerTag ?: SearchRouter.TAG)
    }

    private fun loadInfo() {
        arguments?.getInt(ANIME_ID_KEY)?.let {
            animeViewModel.loadInfoById(it)
            animeViewModel.getUserStateByAnimeId(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeBinding.inflate(inflater, container, false)
        binding.vpStatus.apply {
            adapter = WatchingStatusAdapter()
            setCurrentItem(Pages.NOT_IN_LIST.ordinal, false)
            registerOnPageChangeCallback(viewPagerCallback)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        binding.apply {
            cvBack.setOnClickListener {
                animeViewModel.onBackPressed()
            }
            ivMinus.setOnClickListener {
                val animeId = arguments?.getInt(ANIME_ID_KEY) ?: return@setOnClickListener
                animeViewModel.updateCurrentSeriesForAnimeByDelta(animeId, -1)
            }
            ivPlus.setOnClickListener {
                val animeId = arguments?.getInt(ANIME_ID_KEY) ?: return@setOnClickListener
                animeViewModel.updateCurrentSeriesForAnimeByDelta(animeId, +1)
            }
            etCurrentEpisode.apply {
                setOnEditorActionListener { _, act, _ ->
                    if (act == EditorInfo.IME_ACTION_DONE) {
                        val newAmountOfSeries = verifyInput(text.toString())
                        clearFocus()
                        if (newAmountOfSeries == INCORRECT_SERIES_ET_INPUT_CODE) {
                            snack(requireContext().getString(R.string.incorrect_input))
                            setText(animeViewModel.state.value.userCurrentSeries.toString())
                        } else {
                            val animeId = arguments?.getInt(ANIME_ID_KEY)
                            if (animeId != null) {
                                animeViewModel.updateCurrentSeriesForAnime(
                                    animeId,
                                    newAmountOfSeries,
                                )
                            }
                        }
                    }
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun verifyInput(input: String): Int {
        // todo: Extract to usecase with logic in lists
        return if (input.isNotEmpty() && input.length <= 8 && input.isDigitsOnly() && !input.startsWith(
                '0'
            )
        ) {
            input.toInt()
        } else {
            INCORRECT_SERIES_ET_INPUT_CODE
        }
    }

    private fun subscribeUI() = subscribe {
        animeViewModel.state.collect { state: UiState ->
            Log.d(TAG, state.animeTitle)
            handleState(state)
        }
    }

    private fun handleState(state: UiState) {
        binding.apply {
            txtTitleName.text = state.animeTitle
            txtMark.text = (state.averageScore ?: "?").toString()
            txtTimeToNewEpisode.text = (state.timeToNewEpisode ?: getString(R.string.done))

            state.userWatchingStatus?.let {
                vpStatus.post {
                    vpStatus.setCurrentItem(it, true)
                }
            }
            etCurrentEpisode.setText(state.userCurrentSeries.toString())

            if (state.amountOfSeries == null) {
                txtAmountOfSeries.text = getString(R.string.ongoing)
                txtAmountOfSeriesPostfix.isVisible = false
            } else {
                txtAmountOfSeries.text = state.amountOfSeries.toString()
                txtAmountOfSeriesPostfix.isVisible = true
            }

            state.description?.let { txtDescription.text = HtmlSpanner().fromHtml(it) }
            if (state.animeImages.isNotEmpty()) {
                Glide.with(requireActivity()).load(state.animeImages.first()).into(ivAnimePreview)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        animeViewModel.onBackPressed()
        return true
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(animeId: Int, routerTag: String): AnimeFragment {
            return AnimeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID_KEY, animeId)
                    putString(ROUTER_TAG, routerTag)
                }
            }
        }
    }
}