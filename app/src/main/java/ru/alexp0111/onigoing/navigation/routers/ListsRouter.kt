package ru.alexp0111.onigoing.navigation.routers

import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.navigation.Screens
import javax.inject.Inject

class ListsRouter @Inject constructor(
    private val router: Router,
) : ILocalRouter {

    override fun setFirstScreen() = router.newRootScreen(Screens.ListsScreen())

    override fun routeBack() = router.exit()

    fun routeToAnimeFragment(animeId: Int) = router.navigateTo(Screens.AnimeScreen(animeId, TAG))

    companion object {
        const val TAG = "TAG_LISTS"
    }

}