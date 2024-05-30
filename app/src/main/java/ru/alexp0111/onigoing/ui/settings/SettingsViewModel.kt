package ru.alexp0111.onigoing.ui.settings

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import ru.alexp0111.onigoing.utils.ColorThemes
import ru.alexp0111.onigoing.utils.SharedPreferenceController
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val router: ProfileRouter,
    private val preferenceController: SharedPreferenceController,
) : ViewModel() {

    fun onBackPressed() {
        router.routeBack()
    }

    fun updateTheme(theme: ColorThemes) {
        preferenceController.setColorTheme(theme)
    }

    fun getCurrentTheme(): ColorThemes {
        return preferenceController.getColorTheme()
    }
}