package ru.alexp0111.onigoing.ui.lists

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.ListsRouter
import ru.alexp0111.onigoing.utils.SharedPreferenceController
import javax.inject.Inject

class ListsViewModel @Inject constructor(
    private val router: ListsRouter,
    private val sharedPreferenceController: SharedPreferenceController,
) : ViewModel() {

    fun onNextButtonClicked() = router.routeBack()
    fun updateSortingSettings(sortingFilter: Pair<SortingCharacteristics, SortingWay>) {
        sharedPreferenceController.setSortingConfig(
            sortingCharacteristics = sortingFilter.first,
            sortingWay = sortingFilter.second,
        )
    }

    fun getSortingSettings(): Pair<SortingCharacteristics, SortingWay> {
        return sharedPreferenceController.getSortingConfig()
    }
}