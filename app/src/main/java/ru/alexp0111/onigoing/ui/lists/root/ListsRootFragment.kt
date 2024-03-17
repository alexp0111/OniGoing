package ru.alexp0111.onigoing.ui.lists.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.databinding.FragmentListsRootBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.base.RootFragment
import javax.inject.Inject

class ListsRootFragment : RootFragment() {

    private lateinit var binding: FragmentListsRootBinding

    @Inject
    override lateinit var viewModel: ListsRootViewModel
    override val fragmentContainerId = R.id.container_lists_root

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListsRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this);
    }
}