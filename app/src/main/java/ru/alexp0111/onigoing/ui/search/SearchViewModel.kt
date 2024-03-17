package ru.alexp0111.onigoing.ui.search

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.SearchRouter
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val router: SearchRouter,
) : ViewModel() {

    fun onNextButtonClicked() = router.routeToAnimeFragment()
}