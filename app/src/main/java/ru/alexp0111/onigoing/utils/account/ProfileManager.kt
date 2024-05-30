package ru.alexp0111.onigoing.utils.account

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

private val KEY_USER_CREDENTIALS = "USER_CREDENTIALS"

@Singleton
class ProfileManager @Inject constructor(
    private val preferences: SharedPreferences,
) {
    fun isUserLoggedIn(): Boolean {
        return getUserThatIsLoggedIn() != null
    }

    fun getCurrentUser(): UserInfo? {
        return getUserThatIsLoggedIn()
    }

    fun addNewUser(userInfo: UserInfo) {
        val users = (getAppUsersAsSet() ?: emptySet()) + userInfo
        val convertedUsers = users.map { Gson().toJson(it) }.toMutableSet()
        preferences.edit().putStringSet(KEY_USER_CREDENTIALS, convertedUsers).apply()
    }

    fun updateUserInfo(userInfo: UserInfo) {
        val users = (getAppUsersAsSet() ?: emptySet()).toMutableSet()
        if (users.find { it.id == userInfo.id } != null) {
            users.removeIf { it.id == userInfo.id }
            users.add(userInfo)
        }
        val convertedUsers = users.map { Gson().toJson(it) }.toMutableSet()
        preferences.edit().putStringSet(KEY_USER_CREDENTIALS, convertedUsers).apply()
    }

    private fun getUserThatIsLoggedIn(): UserInfo? {
        return getAppUsersAsSet()?.find { it.isLoggedIn }
    }

    private fun getAppUsersAsSet(): Set<UserInfo>? {
        val users = preferences.getStringSet(KEY_USER_CREDENTIALS, emptySet()) ?: return null
        return users.map { Gson().fromJson(it, UserInfo::class.java) }.toSet()
    }

    fun logOut() {
        val user = getCurrentUser() ?: return
        updateUserInfo(user.copy(isLoggedIn = false))
    }

    fun findUserByEmailAndPassword(email: String, password: String): UserInfo? {
        val users = getAppUsersAsSet()
        return users?.find { it.email == email && it.password == password }
    }

    companion object {
        const val MIN_PASSWORD_SIZE = 6
    }
}