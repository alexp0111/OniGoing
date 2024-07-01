package ru.alexp0111.onigoing.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.databinding.FragmentSettingsBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.IS_RECREATED
import ru.alexp0111.onigoing.ui.base.BackPressable
import ru.alexp0111.onigoing.utils.ColorThemes
import javax.inject.Inject

class SettingsFragment : Fragment(), BackPressable {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateSelectedColorView(settingsViewModel.getCurrentTheme())
        binding.apply {
            cvBack.setOnClickListener {
                settingsViewModel.onBackPressed()
            }
            cvLogOut.setOnClickListener {
                settingsViewModel.logOut()
                requireActivity().finish()
            }

            cvThemeGreen.setOnClickListener {
                updateThemeValue(ColorThemes.GREEN).also { recreate() }
            }
            cvThemeOrange.setOnClickListener {
                updateThemeValue(ColorThemes.ORANGE).also { recreate() }
            }
            cvThemeBlue.setOnClickListener {
                updateThemeValue(ColorThemes.BLUE).also { recreate() }
            }
            cvThemeRed.setOnClickListener {
                updateThemeValue(ColorThemes.RED).also { recreate() }
            }
        }
    }

    private fun recreate() {
        requireActivity().apply {
            intent.putExtra(IS_RECREATED, true)
            recreate()
        }
    }

    private fun updateThemeValue(theme: ColorThemes) {
        settingsViewModel.updateTheme(theme)
        updateSelectedColorView(theme)
    }

    private fun updateSelectedColorView(theme: ColorThemes) {
        binding.apply {
            ivGreenThemeDone.isVisible = theme == ColorThemes.GREEN
            ivOrangeThemeDone.isVisible = theme == ColorThemes.ORANGE
            ivBlueThemeDone.isVisible = theme == ColorThemes.BLUE
            ivRedThemeDone.isVisible = theme == ColorThemes.RED
        }
    }

    override fun onBackPressed(): Boolean {
        settingsViewModel.onBackPressed()
        return true
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}