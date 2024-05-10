package ru.alexp0111.onigoing.ui.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.alexp0111.onigoing.ui.lists.page.ListPageFragment
import ru.alexp0111.onigoing.ui.lists.page.Pages

class ViewPagerListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val pages = intArrayOf(
        Pages.VIEWED.ordinal,
        Pages.ACTUAL.ordinal,
        Pages.PLANNED.ordinal,
        Pages.STOPPED.ordinal,
    )

    override fun createFragment(position: Int): Fragment = ListPageFragment().apply {
        arguments = Bundle().apply {
            putInt(Pages.PAGE_TAG, position)
        }
    }

    override fun getItemCount(): Int = pages.size
}