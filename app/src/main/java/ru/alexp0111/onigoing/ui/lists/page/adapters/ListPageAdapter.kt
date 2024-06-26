package ru.alexp0111.onigoing.ui.lists.page.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.ItemListAnimeBinding
import ru.alexp0111.onigoing.databinding.ItemListAnimeBottomBinding
import ru.alexp0111.onigoing.ui.lists.SortingCharacteristics
import ru.alexp0111.onigoing.ui.lists.SortingWay
import ru.alexp0111.onigoing.ui.lists.page.utils.MarkPainter

abstract class ListPageBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UserWatchingAnime)
}

enum class StarColors {
    MAIN_ACCENT,
    WHITE,
}

class ListPageAdapter(
    private val fragmentActivity: FragmentActivity,
    private val onItemClicked: (UserWatchingAnime) -> Unit,
    private val onSeriesChanged: (UserWatchingAnime, Int) -> Unit,
    private val onMarkSelected: (UserWatchingAnime) -> Unit,
) : RecyclerView.Adapter<ListPageBaseViewHolder>() {

    private val markPainter = MarkPainter(fragmentActivity)
    private val list: MutableList<UserWatchingAnime> = mutableListOf()
    private var sortingCharacteristics = SortingCharacteristics.MARK
    private var sortingWay = SortingWay.ASC

    inner class ListPageViewHolder(
        private val binding: ItemListAnimeBinding,
    ) : ListPageBaseViewHolder(binding.root) {
        override fun bind(item: UserWatchingAnime) {
            binding.apply {
                txtAnimeTitle.text = item.title
                tvCurrentEpisode.text = item.currentSeries.toString()
                ivMarkForeground.setImageResource(markPainter.getMarkAsResource(item))

                Glide.with(fragmentActivity)
                    .load(item.imageUriString.toUri())
                    .into(ivAnimePreview)
            }

            binding.apply {
                txtAnimeTitle.setOnClickListener { onItemClicked.invoke(item) }
                ivAnimePreview.setOnClickListener { onItemClicked.invoke(item) }
                tvCurrentEpisode.setOnClickListener { onItemClicked.invoke(item) }
                ivMinus.setOnClickListener { updateAmountOfSeriesByDelta(item, -1) }
                ivPlus.setOnClickListener { updateAmountOfSeriesByDelta(item, 1) }
                ivMarkForeground.setOnClickListener { onMarkSelected.invoke(item) }
            }
        }
    }

    inner class ListPageBottomViewHolder(
        private val binding: ItemListAnimeBottomBinding,
    ) : ListPageBaseViewHolder(binding.root) {
        override fun bind(item: UserWatchingAnime) {
            binding.apply {
                txtAnimeTitle.text = item.title
                tvCurrentEpisode.text = item.currentSeries.toString()
                ivMarkForeground.setImageResource(markPainter.getMarkAsResource(item))

                Glide.with(fragmentActivity)
                    .load(item.imageUriString.toUri())
                    .into(ivAnimePreview)
            }

            binding.apply {
                txtAnimeTitle.setOnClickListener { onItemClicked.invoke(item) }
                ivAnimePreview.setOnClickListener { onItemClicked.invoke(item) }
                tvCurrentEpisode.setOnClickListener { onItemClicked.invoke(item) }
                ivMinus.setOnClickListener { updateAmountOfSeriesByDelta(item, -1) }
                ivPlus.setOnClickListener { updateAmountOfSeriesByDelta(item, 1) }
                ivMarkForeground.setOnClickListener { onMarkSelected.invoke(item) }
            }
        }
    }

    private fun updateAmountOfSeriesByDelta(item: UserWatchingAnime, delta: Int) {
        onSeriesChanged.invoke(item, maxOf(item.currentSeries + delta, 0))
    }

    // fixme: If we change state of anime, we can have bugs with it's type, if it were first for example

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageBaseViewHolder {
        return if (viewType == list.lastIndex) {
            ListPageBottomViewHolder(
                ItemListAnimeBottomBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        } else {
            ListPageViewHolder(
                ItemListAnimeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ListPageBaseViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun sortList(
        incomingList: List<UserWatchingAnime>? = null,
        sortingCharacteristics: SortingCharacteristics,
        sortingWay: SortingWay,
    ) {
        if (incomingList != null) {
            list.apply {
                clear()
                addAll(incomingList)
            }
        }
        if (sortingNotChanged(sortingCharacteristics, sortingWay) && incomingList == null) return
        if (sortingWay == SortingWay.ASC) {
            when (sortingCharacteristics) {
                SortingCharacteristics.MARK -> list.sortBy { it.mark }
                SortingCharacteristics.SERIES -> list.sortBy { it.currentSeries }
                SortingCharacteristics.NAME -> list.sortBy { it.title }
                SortingCharacteristics.DATE -> list.sortBy { it.addingDate }
            }
        } else {
            when (sortingCharacteristics) {
                SortingCharacteristics.MARK -> list.sortByDescending { it.mark }
                SortingCharacteristics.SERIES -> list.sortByDescending { it.currentSeries }
                SortingCharacteristics.NAME -> list.sortByDescending { it.title }
                SortingCharacteristics.DATE -> list.sortByDescending { it.addingDate }
            }
        }
        updateSortingState(sortingCharacteristics, sortingWay)
    }

    private fun sortingNotChanged(
        characteristics: SortingCharacteristics,
        way: SortingWay,
    ): Boolean {
        return sortingCharacteristics == characteristics && sortingWay == way
    }

    private fun updateSortingState(characteristics: SortingCharacteristics, way: SortingWay) {
        sortingCharacteristics = characteristics
        sortingWay = way
        notifyDataSetChanged()
    }
}