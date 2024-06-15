package ru.alexp0111.onigoing.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentListsBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.lists.page.Pages
import ru.alexp0111.onigoing.ui.lists.page.SortableFragment
import ru.alexp0111.onigoing.ui.lists.page.adapters.ListHeaderAdapter
import javax.inject.Inject

interface SortOrderHandler {
    fun getCurrentSortingFilter(): Pair<SortingCharacteristics, SortingWay>
}

enum class SortingCharacteristics {
    SERIES,
    NAME,
    MARK,
    DATE;

    companion object {
        fun from(ord: Int): SortingCharacteristics {
            return SortingCharacteristics.entries[ord]
        }
    }
}

enum class SortingWay {
    ASC,
    DESC;

    companion object {
        fun from(ord: Int): SortingWay {
            return SortingWay.entries[ord]
        }
    }
}

class ListsFragment : Fragment(), SortOrderHandler {

    @Inject
    lateinit var viewModel: ListsViewModel

    private val headerViewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.vpHeaders.setCurrentItem(Pages.from(position).ordinal, true)
        }
    }

    private val pagesViewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.vpLists.setCurrentItem(Pages.from(position).ordinal, true)
        }
    }

    private val spinnerAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_filter_item,
            R.id.txt_sorting_option,
            resources.getStringArray(R.array.sorting_filters)
        )
    }

    private var currentSortingFilter = Pair(SortingCharacteristics.MARK, SortingWay.ASC)

    private val spinnerItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            currentSortingFilter = when (p2) {
                0 -> currentSortingFilter.copy(first = SortingCharacteristics.SERIES)
                1 -> currentSortingFilter.copy(first = SortingCharacteristics.NAME)
                2 -> currentSortingFilter.copy(first = SortingCharacteristics.MARK)
                else -> currentSortingFilter.copy(first = SortingCharacteristics.DATE)
            }
            updateSortingSetting()
            notifyChildrenAboutSortingChange()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) = Unit
    }

    private lateinit var binding: FragmentListsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            vpLists.adapter = ViewPagerListAdapter(this@ListsFragment)
            vpHeaders.adapter = ListHeaderAdapter()

            vpLists.setCurrentItem(Pages.ACTUAL.ordinal, false)
            vpHeaders.setCurrentItem(Pages.ACTUAL.ordinal, false)

            vpLists.registerOnPageChangeCallback(headerViewPagerCallback)
            vpHeaders.registerOnPageChangeCallback(pagesViewPagerCallback)

            spinnerSortingOrder.adapter = spinnerAdapter
            spinnerSortingOrder.onItemSelectedListener = spinnerItemSelectedListener

            ivSortAscending.setOnClickListener { handleSortingWayAction() }
            ivSortDescending.setOnClickListener { handleSortingWayAction() }
        }
        setSortingSettings()
    }

    private fun setSortingSettings() {
        currentSortingFilter = viewModel.getSortingSettings()
        binding.apply {
            spinnerSortingOrder.post { spinnerSortingOrder.setSelection(currentSortingFilter.first.ordinal) }
            ivSortAscending.isVisible = currentSortingFilter.second == SortingWay.ASC
            ivSortDescending.isVisible = currentSortingFilter.second == SortingWay.DESC
        }
    }

    private fun handleSortingWayAction() {
        binding.apply {
            ivSortAscending.isVisible = !ivSortAscending.isVisible
            ivSortDescending.isVisible = !ivSortDescending.isVisible
            currentSortingFilter = if (ivSortAscending.isVisible) {
                currentSortingFilter.copy(second = SortingWay.ASC)
            } else {
                currentSortingFilter.copy(second = SortingWay.DESC)
            }
        }
        updateSortingSetting()
        notifyChildrenAboutSortingChange()
    }

    private fun updateSortingSetting() {
        viewModel.updateSortingSettings(currentSortingFilter)
    }

    private fun notifyChildrenAboutSortingChange() {
        childFragmentManager.fragments.forEach {
            (it as? SortableFragment)?.notifySortingFilterChanged()
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    override fun getCurrentSortingFilter(): Pair<SortingCharacteristics, SortingWay> {
        return currentSortingFilter
    }
}