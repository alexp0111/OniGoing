package ru.alexp0111.onigoing.ui.profile.login

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import ru.alexp0111.onigoing.utils.account.ProfileManager
import ru.alexp0111.onigoing.utils.account.UserInfo
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val router: ProfileRouter,
    private val profileManager: ProfileManager,
) : ViewModel() {

    fun tryToLogIn(email: String, password: String): Boolean {
        val user: UserInfo = profileManager.findUserByEmailAndPassword(email, password) ?: return false
        profileManager.updateUserInfo(user.copy(isLoggedIn = true))
        goToProfile()
        return true
    }

    private fun goToProfile() {
        router.replaceWithProfile()
    }

}