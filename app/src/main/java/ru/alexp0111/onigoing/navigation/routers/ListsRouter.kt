package ru.alexp0111.onigoing.navigation.routers

import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.navigation.Screens
import javax.inject.Inject

class ListsRouter @Inject constructor(
    private val router: Router,
) : ILocalRouter {

    override fun setFirstScreen() = router.newRootScreen(Screens.ListsScreen())

    override fun routeBack() = router.exit()
}