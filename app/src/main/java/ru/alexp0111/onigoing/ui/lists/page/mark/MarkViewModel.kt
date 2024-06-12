package ru.alexp0111.onigoing.ui.lists.page.mark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import javax.inject.Inject

class MarkViewModel @Inject constructor(
    private val userWatchingAnimeRepository: UserWatchingAnimeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun updateUsersAnime(anime: UserWatchingAnime) {
        viewModelScope.launch(Dispatchers.IO) {
            userWatchingAnimeRepository.updateAnimeState(anime)
            _uiState.update { it.copy(anime = anime) }
        }
    }
}

data class UiState(
    var anime: UserWatchingAnime? = null,
)