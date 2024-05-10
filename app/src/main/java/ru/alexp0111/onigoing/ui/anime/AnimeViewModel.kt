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
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val router: SearchRouter,
    private val repository: AnilistRepository,
    private val userWatchingAnimeRepository: UserWatchingAnimeRepository,
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

    fun getUserStateByAnimeId(animeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val storedAnime = userWatchingAnimeRepository.getAnimeByID(animeId)
            _uiState.update { it.copy(isUserDBResponseSuccess = true) }
            storedAnime?.apply {
                _uiState.update { state ->
                    state.copy(
                        userWatchingStatus = watchingState,
                        userCurrentSeries = currentSeries,
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        router.routeBack()
    }

    fun updateStatusForAnime(animeId: Int, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userWatchingAnimeRepository.getAnimeByID(animeId) != null) {
                userWatchingAnimeRepository.updateAnimeStateById(animeId, status)
            } else if (!state.value.isLoading && state.value.isResponseSuccess) {
                val imageUri = if (state.value.animeImages.isNotEmpty()) {
                    state.value.animeImages.first().toString()
                } else {
                    Uri.EMPTY.toString()
                }
                userWatchingAnimeRepository.insertNewAnime(
                    UserWatchingAnime(
                        id = animeId,
                        title = state.value.animeTitle,
                        imageUriString = imageUri,
                        mark = 0,
                        currentSeries = 0,
                        watchingState = status,
                        addingDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    )
                )
            }
        }
    }
}

data class UiState(
    var animeTitle: String = "",
    var animeImages: List<Uri> = emptyList(),
    var amountOfSeries: Int? = null,
    var userCurrentSeries: Int = 0,
    var userWatchingStatus: Int? = null,
    var averageScore: Int? = null,
    var timeToNewEpisode: String? = null,
    var description: String? = null,
    var isLoading: Boolean = false,
    var isResponseSuccess: Boolean = false,
    var isUserDBResponseSuccess: Boolean = false,
)