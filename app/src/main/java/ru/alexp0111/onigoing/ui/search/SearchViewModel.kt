package ru.alexp0111.onigoing.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.anilist.api.AnilistRepository
import ru.alexp0111.onigoing.anilist.data.Search
import ru.alexp0111.onigoing.anilist.type.MediaType
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import javax.inject.Inject

// TODO: Handle error properly

private const val INTER_FETCH_DELAY = 1000L

class SearchViewModel @Inject constructor(
    private val router: SearchRouter,
    private val repository: AnilistRepository,
) : ViewModel() {

    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(UiState(emptyList()))
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun fetchResultsByString(
        name: String,
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(INTER_FETCH_DELAY)
            repository.fetchSearch(
                MediaType.ANIME,
                10,
                name,
            ).collect { incomingResults ->
                when {
                    incomingResults.isFailure -> {

                    }

                    incomingResults.isSuccess -> {
                        _uiState.update { it.copy(listOfResults = incomingResults.getOrThrow()) }
                    }
                }
            }
        }
    }

    fun onNextButtonClicked() = router.routeToAnimeFragment()
}

data class UiState(
    val listOfResults: List<Search>,
)