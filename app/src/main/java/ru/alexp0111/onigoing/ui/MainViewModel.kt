package ru.alexp0111.onigoing.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val loggedIn = flow { emit(true) }
}