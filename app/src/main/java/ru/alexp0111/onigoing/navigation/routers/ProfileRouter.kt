package ru.alexp0111.onigoing.navigation.routers

import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.navigation.Screens
import javax.inject.Inject

class ProfileRouter @Inject constructor(
    private val router: Router,
) : ILocalRouter {

    override fun setFirstScreen() = router.newRootScreen(Screens.ProfileScreen())

    override fun routeBack() = router.exit()

    fun routeToSettingsFragment() = router.navigateTo(Screens.SettingsScreen())

    fun routeToRegistrationFragment() = router.navigateTo(Screens.RegistrationScreen())
}