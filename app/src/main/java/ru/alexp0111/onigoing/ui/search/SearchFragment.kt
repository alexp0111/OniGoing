package ru.alexp0111.onigoing.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentSearchBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.utils.subscribe
import javax.inject.Inject

private const val KEY_SEARCH_TEXT = "search_text"

class SearchFragment : Fragment() {

    @Inject
    lateinit var searchAnimeAdapterFactory: SearchAnimeAdapterFactory

    @Inject
    lateinit var searchHistoryAdapterFactory: SearchHistoryAdapterFactory

    @Inject
    lateinit var viewModel: SearchViewModel

    private val searchAnimeAdapter by lazy {
        searchAnimeAdapterFactory.create(requireActivity()) {
            searchHistoryAdapter.updateList()
            viewModel.openAnimeWithId(it.id)
        }
    }

    private val searchHistoryAdapter by lazy {
        searchHistoryAdapterFactory {
            binding.etSearch.apply {
                setText(it)
                setSelection(it.length)
            }
        }
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        savedInstanceState?.apply {
            binding.etSearch.apply {
                setText(getString(KEY_SEARCH_TEXT))
                if (text.isNullOrEmpty()) {
                    viewModel.resetSearchIcon()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        binding.apply {
            ivSearchClear.setOnClickListener {
                viewModel.resetToDefaults()
                etSearch.text.clear()
                etSearch.clearFocus()
                hideKeyboard(it)
            }
            ivRefresh.setOnClickListener {
                viewModel.fetchResultsByString(etSearch.text.toString())
            }

            rvSearchItems.adapter = searchAnimeAdapter
            rvSearchItems.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }

            rvSearchHistory.adapter = searchHistoryAdapter
            rvSearchHistory.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    private fun subscribeUI() = subscribe {
        viewModel.state.collect { state: UiState ->
            searchAnimeAdapter.updateList(state.listOfResults)
            binding.apply {
                if (state.shouldClearFocus) etSearch.clearFocus()
                handleSearchBarIcon(state)
                handleResponseInfoMessage(state)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.apply {
            addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    viewModel.resetToDefaults()
                } else {
                    viewModel.fetchResultsByString(it.toString())
                }
            }
            setOnFocusChangeListener { view, isInFocus ->
                binding.rvSearchHistory.isVisible = isInFocus
                if (!isInFocus) {
                    hideKeyboard(view)
                }
                viewModel.updateFocusValue(isInFocus)
            }
            setOnEditorActionListener { _, act, _ ->
                if (act == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    viewModel.updateFocusValue(false)
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun handleResponseInfoMessage(state: UiState) = binding.apply {
        val shouldShow = state.isContainErrors || state.isEmptyResult
        ivResponseInfo.isVisible = shouldShow
        txtResponseInfo.isVisible = shouldShow
        rvSearchItems.isVisible = !shouldShow
        if (!shouldShow) return@apply

        if (state.isContainErrors) {
            ivResponseInfo.setImageResource(R.drawable.server_error)
            txtResponseInfo.text = getString(R.string.something_went_wrong)
        } else {
            ivResponseInfo.setImageResource(R.drawable.nothing_found)
            txtResponseInfo.text = getString(R.string.no_results)
        }
    }

    private fun handleSearchBarIcon(state: UiState) = binding.apply {
        ivSearch.isVisible = state.isDefaultEditTextState
        ivSearchClear.isVisible = state.isClearEditTextState
        ivRefresh.isVisible = state.isContainErrors
        pbSearch.isVisible = state.isLoading
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(
            Activity.INPUT_METHOD_SERVICE,
        ) as? InputMethodManager ?: return

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.etSearch.text?.toString()?.let {
            if (it.isNotEmpty()) {
                outState.putString(KEY_SEARCH_TEXT, it)
            }
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this);
    }
}