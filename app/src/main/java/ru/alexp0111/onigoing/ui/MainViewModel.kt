package ru.alexp0111.onigoing.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import ru.alexp0111.onigoing.utils.ColorThemes
import ru.alexp0111.onigoing.utils.SharedPreferenceController
import ru.alexp0111.onigoing.utils.account.ProfileManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val preferenceController: SharedPreferenceController,
    private val profileManager: ProfileManager,
) : ViewModel() {

    val loggedIn = flow { emit(profileManager.isUserLoggedIn()) }

    fun getColorTheme(): ColorThemes {
        return preferenceController.getColorTheme()
    }
}