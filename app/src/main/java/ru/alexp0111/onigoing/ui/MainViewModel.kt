package ru.alexp0111.onigoing.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import ru.alexp0111.onigoing.utils.ColorThemes
import ru.alexp0111.onigoing.utils.SharedPreferenceController
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val preferenceController: SharedPreferenceController,
) : ViewModel() {

    val loggedIn = flow { emit(true) }

    fun getColorTheme(): ColorThemes {
        return preferenceController.getColorTheme()
    }

}