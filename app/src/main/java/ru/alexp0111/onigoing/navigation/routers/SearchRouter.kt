package ru.alexp0111.onigoing.navigation.routers

import com.github.terrakok.cicerone.Router
import ru.alexp0111.onigoing.navigation.Screens
import javax.inject.Inject

class SearchRouter @Inject constructor(
    private val router: Router,
) : ILocalRouter {

    override fun setFirstScreen() = router.newRootScreen(Screens.SearchScreen())

    override fun routeBack() = router.exit()

    fun routeToAnimeFragment(animeId: Int) = router.navigateTo(Screens.AnimeScreen(animeId))

    fun routeToCommentsFragment() = router.navigateTo(Screens.CommentsScreen())

    fun routeToNotificationsFragment() = router.navigateTo(Screens.NotificationsScreen())
}