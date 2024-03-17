package ru.alexp0111.onigoing.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.alexp0111.onigoing.ui.lists.ListsFragment
import ru.alexp0111.onigoing.ui.lists.root.ListsRootFragment
import ru.alexp0111.onigoing.ui.profile.ProfileFragment
import ru.alexp0111.onigoing.ui.profile.root.ProfileRootFragment
import ru.alexp0111.onigoing.ui.search.SearchFragment
import ru.alexp0111.onigoing.ui.search.root.SearchRootFragment

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

    fun RegistrationScreen() = FragmentScreen { ProfileFragment() }

    fun SettingsScreen() = FragmentScreen { ProfileFragment() }

    fun CommentsScreen() = FragmentScreen { ProfileFragment() }

    fun NotificationsScreen() = FragmentScreen { ProfileFragment() }

    fun AnimeScreen() = FragmentScreen { ProfileFragment() }
}