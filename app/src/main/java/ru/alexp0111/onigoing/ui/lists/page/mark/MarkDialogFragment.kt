package ru.alexp0111.onigoing.ui.lists.page.mark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.DialogFragmentMarkBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.lists.page.utils.MarkPainter
import javax.inject.Inject

private const val MARK_ITEM_INFO_TAG = "MARK_ITEM_INFO_TAG"

class MarkDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: MarkViewModel

    private val markPainter by lazy { MarkPainter(requireActivity()) }
    private var itemInfo: UserWatchingAnime? = null

    private lateinit var binding: DialogFragmentMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        super.onCreate(savedInstanceState)
        arguments?.getString(MARK_ITEM_INFO_TAG)?.let {
            itemInfo = Gson().fromJson(it, UserWatchingAnime::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentMarkBinding.inflate(inflater, container, false)
        itemInfo?.let { item ->
            applyToStars { view, index ->
                markPainter.paintStarIfNecessary(view, item, index)
                view.setOnClickListener { updateMark(index, item) }
            }
        }
        return binding.root
    }

    private fun applyToStars(lambda: (view: View, index: Int) -> Unit) {
        binding.glDialogMark.children.apply {
            forEachIndexed { index, view ->
                lambda.invoke(view, index)
            }
        }
    }

    private fun updateMark(index: Int, item: UserWatchingAnime) {
        val mark = index + 1
        viewModel.updateUsersAnime(item.copy(mark = mark))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
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
        state.anime?.let { item ->
            itemInfo = item
            applyToStars { view, index ->
                markPainter.paintStarIfNecessary(view, item, index)
            }
        }
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(item: UserWatchingAnime): MarkDialogFragment {
            return MarkDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(MARK_ITEM_INFO_TAG, Gson().toJson(item))
                }
            }
        }
    }
}