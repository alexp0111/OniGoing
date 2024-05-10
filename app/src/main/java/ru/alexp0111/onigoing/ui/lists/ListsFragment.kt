package ru.alexp0111.onigoing.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.alexp0111.onigoing.databinding.FragmentListsBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.lists.page.Pages
import javax.inject.Inject

class ListsFragment : Fragment() {


    @Inject
    lateinit var viewModel: ListsViewModel

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
            vpLists.setCurrentItem(Pages.ACTUAL.ordinal, false)
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }
}