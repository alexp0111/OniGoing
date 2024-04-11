package ru.alexp0111.onigoing.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.databinding.FragmentSearchBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import javax.inject.Inject

private const val KEY_SEARCH_TEXT = "search_text"

// TODO: Loading animation in search button place
// TODO: Goto Anime Screen & load anime info

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModel: SearchViewModel

    private val searchAnimeAdapter by lazy {
        SearchAnimeAdapter(requireActivity()) {
            Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
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
            binding.etSearch.setText(getString(KEY_SEARCH_TEXT))
            handleSearchButtonVisibility(binding.etSearch.text)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        binding.apply {
            etSearch.addTextChangedListener {
                handleSearchButtonVisibility(it)
                viewModel.fetchResultsByString(it.toString())
            }
            ivSearchClear.setOnClickListener {
                etSearch.text.clear()
                etSearch.clearFocus()
                hideKeyboard(it)
            }

            rvSearchItems.adapter = searchAnimeAdapter
            rvSearchItems.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state: UiState ->
                        searchAnimeAdapter.updateList(state.listOfResults)
                    }
                }
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(
            Activity.INPUT_METHOD_SERVICE,
        ) as? InputMethodManager ?: return

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun handleSearchButtonVisibility(searchText: Editable?) {
        binding.apply {
            if (searchText.isNullOrEmpty()) {
                ivSearch.isVisible = true
                ivSearchClear.isVisible = false
            } else {
                ivSearch.isVisible = false
                ivSearchClear.isVisible = true
            }
        }
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