package ru.alexp0111.onigoing.ui.settings

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val router: ProfileRouter,
) : ViewModel() {

    fun onBackPressed() {
        router.routeBack()
    }
}