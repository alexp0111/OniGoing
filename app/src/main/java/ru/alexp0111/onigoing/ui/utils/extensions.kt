package ru.alexp0111.onigoing.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> AppCompatActivity.collectOnLifecycle(
    flow: Flow<T>,
    targetState: Lifecycle.State = Lifecycle.State.CREATED,
    onEach: (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(targetState) {
            flow.collect { onEach(it) }
        }
    }
}

fun Fragment.subscribe(lambda: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                lambda(this)
            }
        }
    }
}