package ru.alexp0111.onigoing.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.alexp0111.onigoing.ui.lists.SortingCharacteristics
import ru.alexp0111.onigoing.ui.lists.SortingWay
import javax.inject.Inject
import javax.inject.Singleton

private const val SEARCH_HISTORY_MAX_SIZE = 5

private const val KEY_SEARCH_HISTORY = "SEARCH_HISTORY"
private const val KEY_COLOR_THEME = "COLOR_THEME"
private const val KEY_SORTING_CHARACTERISTICS = "SORTING_CHARACTERISTICS"
private const val KEY_SORTING_WAY = "SORTING_WAY"

enum class ColorThemes {
    GREEN,
    ORANGE,
    BLUE,
    RED;

    companion object {
        fun from(ord: Int): ColorThemes {
            return entries[ord]
        }
    }
}

@Singleton
class SharedPreferenceController @Inject constructor(
    private val preferences: SharedPreferences,
) {
    fun insertNewHistoryElement(record: String): Boolean {
        val currentHistoryState = getSearchHistory().historyList
        if (record in currentHistoryState) return false
        if (currentHistoryState.size == SEARCH_HISTORY_MAX_SIZE) {
            currentHistoryState.removeLast()
        }
        currentHistoryState.add(0, record)
        preferences.edit()
            .putString(KEY_SEARCH_HISTORY, Gson().toJson(SearchHistory(currentHistoryState)))
            .apply()
        return true
    }

    fun removeHistoryElementWithIndex(index: Int) {
        val currentHistoryState = getSearchHistory().historyList
        currentHistoryState.removeAt(index)
        preferences.edit()
            .putString(KEY_SEARCH_HISTORY, Gson().toJson(SearchHistory(currentHistoryState)))
            .apply()
    }

    fun getSearchHistory(): SearchHistory {
        return Gson().fromJson(
            preferences.getString(
                KEY_SEARCH_HISTORY, Gson().toJson(SearchHistory(mutableListOf()))
            ),
            SearchHistory::class.java,
        )
    }

    fun setColorTheme(theme: ColorThemes) {
        preferences.edit().putInt(KEY_COLOR_THEME, theme.ordinal).apply()
    }

    fun getColorTheme(): ColorThemes {
        val ord = preferences.getInt(KEY_COLOR_THEME, 0)
        return ColorThemes.from(ord)
    }

    fun setSortingConfig(sortingCharacteristics: SortingCharacteristics, sortingWay: SortingWay) {
        preferences.edit()
            .putInt(KEY_SORTING_CHARACTERISTICS, sortingCharacteristics.ordinal)
            .putInt(KEY_SORTING_WAY, sortingWay.ordinal).apply()
    }

    fun getSortingConfig(): Pair<SortingCharacteristics, SortingWay> {
        val characteristics =  preferences.getInt(KEY_SORTING_CHARACTERISTICS, 0)
        val way =  preferences.getInt(KEY_SORTING_WAY, 0)
        return Pair(SortingCharacteristics.from(characteristics), SortingWay.from(way))
    }
}