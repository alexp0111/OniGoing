package ru.alexp0111.onigoing.navigation.routers

import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.navigation.Screens
import ru.alexp0111.onigoing.utils.account.ProfileManager
import javax.inject.Inject

class ProfileRouter @Inject constructor(
    private val router: Router,
    private val profileManager: ProfileManager,
) : ILocalRouter {

    override fun setFirstScreen() {
        if (profileManager.isUserLoggedIn()) {
            router.newRootScreen(Screens.ProfileScreen())
        } else {
            router.newRootScreen(Screens.LogInScreen())
        }
    }

    override fun routeBack() = router.exit()

    fun routeToSettingsFragment() = router.navigateTo(Screens.SettingsScreen())

    fun replaceWithProfile() {
        router.newRootScreen(Screens.ProfileScreen())
    }

    companion object {
        const val TAG = "TAG_PROFILE"
    }
}