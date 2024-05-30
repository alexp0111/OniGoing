package ru.alexp0111.onigoing.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.alexp0111.onigoing.ui.anime.AnimeFragment
import ru.alexp0111.onigoing.ui.lists.ListsFragment
import ru.alexp0111.onigoing.ui.lists.root.ListsRootFragment
import ru.alexp0111.onigoing.ui.profile.ProfileFragment
import ru.alexp0111.onigoing.ui.profile.login.LogInFragment
import ru.alexp0111.onigoing.ui.profile.root.ProfileRootFragment
import ru.alexp0111.onigoing.ui.search.SearchFragment
import ru.alexp0111.onigoing.ui.search.root.SearchRootFragment
import ru.alexp0111.onigoing.ui.settings.SettingsFragment

object Screens {

    fun RootScreen(tabTag: String) = FragmentScreen {
        when (tabTag) {
            NavigationTabTags.TAG_SEARCH -> SearchRootFragment()
            NavigationTabTags.TAG_LISTS -> ListsRootFragment()
            else -> ProfileRootFragment()
        }
    }

    fun SearchScreen() = FragmentScreen { SearchFragment() }

    fun ListsScreen() = FragmentScreen { ListsFragment() }

    fun ProfileScreen() = FragmentScreen { ProfileFragment() }

    // todo:

    fun LogInScreen() = FragmentScreen { LogInFragment() }

    fun SettingsScreen() = FragmentScreen { SettingsFragment() }

    fun CommentsScreen() = FragmentScreen { ProfileFragment() }

    fun NotificationsScreen() = FragmentScreen { ProfileFragment() }

    fun AnimeScreen(animeId: Int) = FragmentScreen("AnimeScreen_$animeId") {
        AnimeFragment.newInstance(animeId)
    }
}