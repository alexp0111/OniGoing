package ru.alexp0111.onigoing.ui.lists.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.databinding.FragmentListPageBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.lists.SortOrderHandler
import javax.inject.Inject

interface SortableFragment {
    fun notifySortingFilterChanged()
}

class ListPageFragment : Fragment(), SortableFragment {

    @Inject
    lateinit var viewModel: ListPageViewModel

    private val listsAdapter by lazy {
        ListPageAdapter(requireActivity(), { onRootResult ->
            // react to click
        }, { onMinusResult ->
            val newCurrentSeries = maxOf(onMinusResult.currentSeries - 1, 0)
            viewModel.updateUsersAnime(onMinusResult.copy(currentSeries = newCurrentSeries))
        }, { onPlusResult ->
            val newCurrentSeries = onPlusResult.currentSeries + 1
            viewModel.updateUsersAnime(onPlusResult.copy(currentSeries = newCurrentSeries))

        })
    }

    private lateinit var binding: FragmentListPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        viewModel.loadUserListForState(
            arguments?.getInt(
                Pages.PAGE_TAG,
                Pages.ACTUAL.ordinal,
            ) ?: 0
        )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        binding.apply {
            rvLists.adapter = listsAdapter
            rvLists.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handleSortingUpdate()
    }

    override fun notifySortingFilterChanged() {
        handleSortingUpdate()
    }

    private fun handleSortingUpdate() {
        val filter = (parentFragment as SortOrderHandler).getCurrentSortingFilter()
        listsAdapter.sortList(
            sortingCharacteristics = filter.first,
            sortingWay = filter.second,
        )
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state: UiState ->
                        handleState(state)
                    }
                }
            }
        }
    }

    private fun handleState(state: UiState) {
        binding.apply {
            listsAdapter.updateList(state.listOfTitles)
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}