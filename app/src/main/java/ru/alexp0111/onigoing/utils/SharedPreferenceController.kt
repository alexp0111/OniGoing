package ru.alexp0111.onigoing.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_SEARCH_HISTORY = "SEARCH_HISTORY"
private const val SEARCH_HISTORY_MAX_SIZE = 5

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
}