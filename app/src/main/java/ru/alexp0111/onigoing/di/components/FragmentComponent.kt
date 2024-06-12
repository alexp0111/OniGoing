package ru.alexp0111.onigoing.di.components

import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.ui.anime.AnimeFragment
import ru.alexp0111.onigoing.ui.lists.ListsFragment
import ru.alexp0111.onigoing.ui.lists.page.ListPageFragment
import ru.alexp0111.onigoing.ui.lists.page.mark.MarkDialogFragment
import ru.alexp0111.onigoing.ui.lists.root.ListsRootFragment
import ru.alexp0111.onigoing.ui.profile.ProfileFragment
import ru.alexp0111.onigoing.ui.profile.login.LogInFragment
import ru.alexp0111.onigoing.ui.profile.root.ProfileRootFragment
import ru.alexp0111.onigoing.ui.search.root.SearchRootFragment
import ru.alexp0111.onigoing.ui.search.SearchFragment
import ru.alexp0111.onigoing.ui.settings.SettingsFragment

interface FragmentComponent {

    fun inject(fragment: ProfileRootFragment)
    fun inject(fragment: ProfileFragment)

    fun inject(fragment: SearchRootFragment)
    fun inject(fragment: SearchFragment)

    fun inject(fragment: ListsRootFragment)
    fun inject(fragment: ListsFragment)

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: AnimeFragment)

    fun inject(fragment: ListPageFragment)

    fun inject(fragment: LogInFragment)

    fun inject(fragment: MarkDialogFragment)

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}