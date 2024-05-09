package ru.alexp0111.onigoing.ui.anime

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.alexp0111.onigoing.databinding.FragmentAnimeBinding
import ru.alexp0111.onigoing.di.components.FragmentComponent
import ru.alexp0111.onigoing.ui.base.BackPressable
import javax.inject.Inject

private const val TAG = "AnimeFragment"
private const val ANIME_ID_KEY = "ANIME_ID_KEY"

// todo: 1. loading
//       2. Few images in preview
//       3. Normal episodes & airingTime check
//       4. Current episode logic

class AnimeFragment : Fragment(), BackPressable {

    @Inject
    lateinit var animeViewModel: AnimeViewModel

    private lateinit var binding: FragmentAnimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectSelf()
        arguments?.getInt(ANIME_ID_KEY)?.let {
            animeViewModel.loadInfoById(it)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        binding.apply {
            cvBack.setOnClickListener {
                animeViewModel.onBackPressed()
            }
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    animeViewModel.state.collect { state: UiState ->
                        Log.d(TAG, state.animeTitle)
                        handleState(state)
                    }
                }
            }
        }
    }

    private fun handleState(state: UiState) {
        binding.apply {
            txtTitleName.text = state.animeTitle
            txtMark.text = (state.averageScore ?: "?").toString()
            txtTimeToNewEpisode.text = (state.timeToNewEpisode ?: "No info")

            state.amountOfSeries?.let {
                txtAmountOfSeries.text = it.toString()
            } // fixme: Incorrect data
            state.description?.let { txtDescription.text = it }
            if (state.animeImages.isNotEmpty()) {
                Glide.with(requireActivity()).load(state.animeImages.first()).into(ivAnimePreview)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        animeViewModel.onBackPressed()
        return true
    }

    private fun injectSelf() {
        FragmentComponent.from(this).inject(this)
    }

    companion object {
        fun newInstance(animeId: Int): AnimeFragment {
            return AnimeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID_KEY, animeId)
                }
            }
        }
    }
}