package ru.alexp0111.onigoing.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.database.user_watching_anime.UserWatchingAnimeRepository
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.navigation.routers.ProfileRouter
import ru.alexp0111.onigoing.ui.lists.page.Pages
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val context: Context,
    private val router: ProfileRouter,
    private val userWatchingAnimeRepository: UserWatchingAnimeRepository
) : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun goToSettingsFragment() {
        router.routeToSettingsFragment()
    }

    fun loadUsersStatistics() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            userWatchingAnimeRepository.getAllInfo().collect { incomingResult ->
                _uiState.update {
                    it.copy(
                        pieData = extractPieDataFromList(incomingResult),
                        totalEpisodes = extractTotalEpisodes(incomingResult),
                        totalTitles = extractTotalTitles(incomingResult),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun extractTotalTitles(incomingResult: List<UserWatchingAnime>): Int {
        return filterWatched(incomingResult).size
    }

    private fun extractTotalEpisodes(incomingResult: List<UserWatchingAnime>): Int {
        return filterWatched(incomingResult).sumOf { it.currentSeries }
    }

    private fun filterWatched(incomingResult: List<UserWatchingAnime>): List<UserWatchingAnime> {
        return incomingResult.filter {
            it.watchingState == Pages.VIEWED.ordinal
                    || it.watchingState == Pages.ACTUAL.ordinal
                    || it.watchingState == Pages.STOPPED.ordinal
        }
    }

    private fun extractPieDataFromList(incomingResult: List<UserWatchingAnime>): PieData {
        val pieEntries = mutableListOf<PieEntry>().apply {
            add(PieEntry(incomingResult.filter { it.watchingState == Pages.VIEWED.ordinal }.size.toFloat()))
            add(PieEntry(incomingResult.filter { it.watchingState == Pages.ACTUAL.ordinal }.size.toFloat()))
            add(PieEntry(incomingResult.filter { it.watchingState == Pages.PLANNED.ordinal }.size.toFloat()))
            add(PieEntry(incomingResult.filter { it.watchingState == Pages.STOPPED.ordinal }.size.toFloat()))
        }

        val pieDataSet = PieDataSet(pieEntries, "User title statistics").apply {
            colors = context.resources.getIntArray(R.array.chart_colors).toList()
            setDrawValues(false)
        }
        return PieData(pieDataSet)
    }
}

data class UiState(
    var pieData: PieData? = null,
    var totalEpisodes: Int? = null,
    var totalTitles: Int? = null,
    var isLoading: Boolean = false,
)