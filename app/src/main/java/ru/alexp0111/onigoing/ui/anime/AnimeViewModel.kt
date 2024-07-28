package ru.alexp0111.onigoing.ui.anime

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.anilist.api.AnilistRepository
import ru.alexp0111.onigoing.anilist.data.AnimeMedia
import ru.alexp0111.onigoing.anilist.type.MediaSeason
import ru.alexp0111.onigoing.anilist.type.MediaStatus
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.navigation.routers.ListsRouter
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import ru.alexp0111.onigoing.ui.lists.page.Pages
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AssistedFactory
interface AnimeViewModelFactory {
    fun create(routerTag: String): AnimeViewModel
}

class AnimeViewModel @AssistedInject constructor(
    @Assisted private val routerTag: String,
    private val searchRouter: SearchRouter,
    private val listsRouter: ListsRouter,
    private val repository: AnilistRepository,
    private val userWatchingAnimeRepository: UserWatchingAnimeRepository,
) : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    private val _uiUserState = MutableStateFlow(UiUserState())
    val userState: StateFlow<UiUserState>
        get() = _uiUserState.asStateFlow()

    fun loadInfoById(animeId: Int) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            repository.fetchAnimeMedia(animeId).collect { incomingResult ->
                when {
                    incomingResult.isFailure -> handleFailureResult()
                    incomingResult.isSuccess -> updateStateWithResult(incomingResult.getOrThrow())
                }
            }
        }
    }

    private fun handleFailureResult() {
        _uiState.update {
            it.copy(isLoading = false, isResponseSuccess = false)
        }
    }

    private fun updateStateWithResult(result: AnimeMedia) {
        _uiState.update {
            it.copy(
                animeTitle = result.title ?: "",
                animeImages = listOf(result.coverImage?.toUri() ?: Uri.EMPTY),
                thumbnail = result.thumbnail?.toUri() ?: Uri.EMPTY,
                amountOfSeries = result.episodes,
                status = result.status,
                season = result.season,
                seasonYear = result.seasonYear,
                averageScore = result.averageScore,
                nextAiringEpisode = result.nextAiringEpisode,
                timeToNewEpisode = result.nextAiringEpisodeSchedule,
                description = result.description,
                isLoading = false,
                isResponseSuccess = true,
            )
        }
    }

    fun updateCurrentSeriesForAnimeByDelta(animeId: Int, deltaSeries: Int) {
        userState.value.userWatchingAnime?.let {
            val newAmountOfSeries = maxOf(it.currentSeries + deltaSeries, 0)
            return updateCurrentSeriesForAnime(animeId, newAmountOfSeries)
        }
        updateCurrentSeriesForAnime(animeId, maxOf(deltaSeries, 0))
    }

    fun updateCurrentSeriesForAnime(animeId: Int, amountOfSeries: Int) {
        updateAnimeField(animeId, amountOfSeries = amountOfSeries)
    }

    fun updateStatusForAnime(animeId: Int, status: Int) {
        val amountOfSeries = if (status == Pages.VIEWED.ordinal && state.value.amountOfSeries != null) {
            state.value.amountOfSeries
        } else null
        updateAnimeField(animeId, amountOfSeries = amountOfSeries, status = status)
    }

    private fun updateAnimeField(animeId: Int, amountOfSeries: Int? = null, status: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            userState.value.userWatchingAnime?.let {
                userWatchingAnimeRepository.updateAnimeState(
                    it.copy(
                        watchingState = status ?: it.watchingState,
                        currentSeries = amountOfSeries ?: it.currentSeries,
                    )
                )
                return@launch getUserStateByAnimeId(animeId)
            }

            if (state.value.isLoading || !state.value.isResponseSuccess) return@launch

            val newItem = makeNewUserWatchingAnimeItem(animeId)
            userWatchingAnimeRepository.insertNewAnime(
                newItem.copy(
                    watchingState = status ?: newItem.watchingState,
                    currentSeries = amountOfSeries ?: newItem.currentSeries,
                )
            )
            getUserStateByAnimeId(animeId)
        }
    }

    fun getUserStateByAnimeId(animeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val storedAnime = userWatchingAnimeRepository.getAnimeByID(animeId)
            _uiUserState.update {
                it.copy(
                    isUserDBResponseSuccess = true,
                    userWatchingAnime = storedAnime,
                )
            }
        }
    }

    private fun makeNewUserWatchingAnimeItem(animeId: Int): UserWatchingAnime {
        return UserWatchingAnime(
            id = animeId,
            title = state.value.animeTitle,
            imageUriString = state.value.thumbnail.toString(),
            mark = 0,
            currentSeries = 0,
            watchingState = Pages.NOT_IN_LIST.ordinal,
            addingDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
        )
    }

    fun getUserCurrentSeriesAsString(): String {
        return (userState.value.userWatchingAnime?.currentSeries ?: 0).toString()
    }

    fun onBackPressed() {
        val currentRouter = when (routerTag) {
            SearchRouter.TAG -> searchRouter
            ListsRouter.TAG -> listsRouter
            else -> null
        }
        currentRouter?.routeBack()
    }
}

data class UiState(
    var animeTitle: String = "",
    var animeImages: List<Uri> = emptyList(),
    var thumbnail: Uri = Uri.EMPTY,
    var amountOfSeries: Int? = null,
    var status: MediaStatus? = null,
    var season: MediaSeason? = null,
    var seasonYear: Int? = null,
    var averageScore: Int? = null,
    var nextAiringEpisode: Int? = null,
    var timeToNewEpisode: String? = null,
    var description: String? = null,
    var isLoading: Boolean = false,
    var isResponseSuccess: Boolean = false,
)

data class UiUserState(
    var userWatchingAnime: UserWatchingAnime? = null,
    var isUserDBResponseSuccess: Boolean = false,
)