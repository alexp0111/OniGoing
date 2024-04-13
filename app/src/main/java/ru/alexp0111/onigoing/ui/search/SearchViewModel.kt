package ru.alexp0111.onigoing.ui.search

import android.util.Log
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

private const val INTER_FETCH_DELAY = 1000L
private const val RESULTS_PER_PAGE = 10

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
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isContainErrors = false,
                    isEmptyResult = false,
                    isDefaultEditTextState = false,
                    isClearEditTextState = false,
                )
            }
            delay(INTER_FETCH_DELAY)
            repository.fetchSearch(
                MediaType.ANIME,
                RESULTS_PER_PAGE,
                name,
            ).collect { incomingResults ->
                when {
                    incomingResults.isFailure -> {
                        _uiState.update {
                            it.copy(
                                listOfResults = emptyList(),
                                isEmptyResult = false,
                                isLoading = false,
                                isContainErrors = true,
                                isDefaultEditTextState = false,
                                isClearEditTextState = false,
                            )
                        }
                    }

                    incomingResults.isSuccess -> {
                        val results = incomingResults.getOrThrow()
                        Log.d("SUS", results.size.toString())
                        _uiState.update {
                            it.copy(
                                listOfResults = results,
                                isEmptyResult = results.isEmpty(),
                                isLoading = false,
                                isContainErrors = false,
                                isClearEditTextState = true,
                                isDefaultEditTextState = false,
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetToDefaults() {
        searchJob?.cancel()
        resetSearchIcon()
        resetList()
    }

    fun resetSearchIcon() {
        _uiState.update {
            it.copy(
                isDefaultEditTextState = true,
                isClearEditTextState = false,
                isEmptyResult = false,
                isLoading = false,
                isContainErrors = false,
            )
        }
    }

    private fun resetList() {
        _uiState.update { it.copy(listOfResults = emptyList()) }
    }

    fun onNextButtonClicked() = router.routeToAnimeFragment()
}

data class UiState(
    val listOfResults: List<Search>,
    var isDefaultEditTextState: Boolean = true,
    var isClearEditTextState: Boolean = false,
    var isEmptyResult: Boolean = false,
    var isLoading: Boolean = false,
    var isContainErrors: Boolean = false,
)