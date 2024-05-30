package ru.alexp0111.onigoing.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentSettingsBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
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
                updateThemeValue(ColorThemes.GREEN)
                showRecreateMessage()
            }
            cvThemeOrange.setOnClickListener {
                updateThemeValue(ColorThemes.ORANGE)
                showRecreateMessage()
            }
            cvThemeBlue.setOnClickListener {
                updateThemeValue(ColorThemes.BLUE)
                showRecreateMessage()
            }
            cvThemeRed.setOnClickListener {
                updateThemeValue(ColorThemes.RED)
                showRecreateMessage()
            }
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

    private fun showRecreateMessage() {
        Snackbar.make(
            requireView(),
            getString(R.string.restart_app_for_theme_snack_bar),
            Snackbar.LENGTH_LONG,
        ).setBackgroundTint(resources.getColor(R.color.dark, requireActivity().theme))
            .setTextColor(resources.getColor(R.color.white, requireActivity().theme))
            .show()
    }

    override fun onBackPressed(): Boolean {
        settingsViewModel.onBackPressed()
        return true
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}