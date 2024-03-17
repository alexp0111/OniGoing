package ru.alexp0111.onigoing.ui.profile

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val router: ProfileRouter,
) : ViewModel() {

    fun onNextButtonClicked() = router.routeToSettingsFragment()
}