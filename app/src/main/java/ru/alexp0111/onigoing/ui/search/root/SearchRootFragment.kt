package ru.alexp0111.onigoing.ui.search.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentSearchRootBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.base.RootFragment
import javax.inject.Inject

class SearchRootFragment : RootFragment() {

    private lateinit var binding: FragmentSearchRootBinding

    @Inject
    override lateinit var viewModel: SearchRootViewModel
    override val fragmentContainerId = R.id.container_search_root

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this);
    }
}