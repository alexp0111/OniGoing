package ru.alexp0111.onigoing.ui.anime

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.anilist.api.AnilistRepository
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val router: SearchRouter,
    private val repository: AnilistRepository,
) : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun loadInfoById(animeId: Int) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            repository.fetchAnimeMedia(animeId).collect { incomingResult ->
                when {
                    incomingResult.isFailure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isResponseSuccess = false
                            )
                        }
                    }

                    incomingResult.isSuccess -> {
                        val result = incomingResult.getOrThrow()
                        _uiState.update {
                            it.copy(
                                animeTitle = result.title ?: "",
                                animeImages = listOf(result.coverImage?.toUri() ?: Uri.EMPTY),
                                amountOfSeries = result.episodes,
                                averageScore = result.averageScore,
                                timeToNewEpisode = result.nextAiringEpisodeSchedule,
                                description = result.description,
                                isLoading = false,
                                isResponseSuccess = true,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onBackPressed() {
        router.routeBack()
    }
}

data class UiState(
    var animeTitle: String = "",
    var animeImages: List<Uri> = emptyList(),
    var amountOfSeries: Int? = null,
    var averageScore: Int? = null,
    var timeToNewEpisode: String? = null,
    var description: String? = null,
    var isLoading: Boolean = false,
    var isResponseSuccess: Boolean = true,
)