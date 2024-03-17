package ru.alexp0111.onigoing.di.components

import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.ui.lists.ListsFragment
import ru.alexp0111.onigoing.ui.lists.root.ListsRootFragment
import ru.alexp0111.onigoing.ui.profile.ProfileFragment
import ru.alexp0111.onigoing.ui.profile.root.ProfileRootFragment
import ru.alexp0111.onigoing.ui.search.root.SearchRootFragment
import ru.alexp0111.onigoing.ui.search.SearchFragment

interface FragmentComponent {

    fun inject(fragment: ProfileRootFragment)
    fun inject(fragment: ProfileFragment)

    fun inject(fragment: SearchRootFragment)
    fun inject(fragment: SearchFragment)

    fun inject(fragment: ListsRootFragment)
    fun inject(fragment: ListsFragment)

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}