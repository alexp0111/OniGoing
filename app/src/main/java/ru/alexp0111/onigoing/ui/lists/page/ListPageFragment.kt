package ru.alexp0111.onigoing.ui.lists.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.FragmentListPageBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.lists.SortOrderHandler
import ru.alexp0111.onigoing.ui.lists.page.adapters.ListPageAdapter
import ru.alexp0111.onigoing.ui.lists.page.mark.MarkDialogFragment
import ru.alexp0111.onigoing.ui.utils.SeriesInputVerifier
import ru.alexp0111.onigoing.ui.utils.subscribe
import ru.alexp0111.onigoing.utils.snack
import javax.inject.Inject

interface SortableFragment {
    fun notifySortingFilterChanged()
}

private const val MARK_DIALOG_TAG = "mark_dialog_1"

class ListPageFragment : Fragment(), SortableFragment {

    @Inject
    lateinit var viewModel: ListPageViewModel

    private lateinit var binding: FragmentListPageBinding

    private val listsAdapter by lazy {
        ListPageAdapter(requireActivity(), { item ->
            viewModel.openAnimeWithId(item.id)
        }, { item, newAmountOfSeries ->
            handleIncomingAmountOfSeries(item, newAmountOfSeries)
        }, { item ->
            MarkDialogFragment.newInstance(item).show(parentFragmentManager, MARK_DIALOG_TAG)
        })
    }

    private fun handleIncomingAmountOfSeries(item: UserWatchingAnime, newAmountOfSeries: Int) {
        if (newAmountOfSeries == SeriesInputVerifier.INCORRECT_SERIES_ET_INPUT_CODE) {
            return snack(requireContext().getString(R.string.incorrect_input))
        }
        viewModel.updateUsersAnime(item.copy(currentSeries = newAmountOfSeries))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        viewModel.loadUserListForState(
            arguments?.getInt(Pages.PAGE_TAG, Pages.ACTUAL.ordinal) ?: 0
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

    override fun notifySortingFilterChanged() {
        updateListWithSorting()
    }

    private fun updateListWithSorting(list: List<UserWatchingAnime>? = null) {
        val filter = (parentFragment as SortOrderHandler).getCurrentSortingFilter()
        listsAdapter.sortList(
            incomingList = list,
            sortingCharacteristics = filter.first,
            sortingWay = filter.second,
        )
    }

    private fun subscribeUI() = subscribe {
        viewModel.state.collect { state: UiState ->
            handleState(state)
        }
    }

    private fun handleState(state: UiState) {
        binding.apply {
            updateListWithSorting(state.listOfTitles)
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}