package ru.alexp0111.onigoing.ui.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import javax.inject.Inject

class ListPageViewModel @Inject constructor(
    private val userWatchingAnimeRepository: UserWatchingAnimeRepository,
) : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun loadUserListForState(state: Int) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            userWatchingAnimeRepository.getAllAnimeWithStatus(state).collect { incomingResult ->
                _uiState.update {
                    it.copy(
                        listOfTitles = incomingResult,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun updateUsersAnime(anime: UserWatchingAnime) {
        viewModelScope.launch(Dispatchers.IO) {
            userWatchingAnimeRepository.updateAnimeState(anime)
        }
    }
}

data class UiState(
    var listOfTitles: List<UserWatchingAnime> = emptyList(),
    var isLoading: Boolean = false,
)