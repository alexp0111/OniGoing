package ru.alexp0111.onigoing.ui.lists

import androidx.lifecycle.ViewModel
import ru.alexp0111.onigoing.navigation.routers.ListsRouter
import javax.inject.Inject

class ListsViewModel @Inject constructor(
    private val router: ListsRouter,
) : ViewModel() {

    fun onNextButtonClicked() = router.routeBack()
}