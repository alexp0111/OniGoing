package ru.alexp0111.onigoing.ui.lists.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.ItemListAnimeBinding
import ru.alexp0111.onigoing.databinding.ItemListAnimeBottomBinding
import ru.alexp0111.onigoing.ui.lists.SortingCharacteristics
import ru.alexp0111.onigoing.ui.lists.SortingWay

abstract class ListPageBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UserWatchingAnime)
}

class ListPageAdapter(
    private val fragmentActivity: FragmentActivity,
    private val onItemClicked: (UserWatchingAnime) -> Unit,
    private val onMinusClicked: (UserWatchingAnime) -> Unit,
    private val onPlusClicked: (UserWatchingAnime) -> Unit,
) : RecyclerView.Adapter<ListPageBaseViewHolder>() {

    private val list: MutableList<UserWatchingAnime> = mutableListOf()
    private var sortingCharacteristics = SortingCharacteristics.MARK
    private var sortingWay = SortingWay.ASC

    inner class ListPageViewHolder(
        private val binding: ItemListAnimeBinding,
    ) : ListPageBaseViewHolder(binding.root) {
        override fun bind(item: UserWatchingAnime) {
            binding.apply {
                txtAnimeTitle.text = item.title
                txtCurrentEpisode.text = item.currentSeries.toString()

                Glide.with(fragmentActivity)
                    .load(item.imageUriString.toUri())
                    .into(ivAnimePreview)
            }

            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                ivMinus.setOnClickListener { onMinusClicked.invoke(item) }
                ivPlus.setOnClickListener { onPlusClicked.invoke(item) }
            }
        }
    }

    inner class ListPageBottomViewHolder(
        private val binding: ItemListAnimeBottomBinding,
    ) : ListPageBaseViewHolder(binding.root) {
        override fun bind(item: UserWatchingAnime) {
            binding.apply {
                txtAnimeTitle.text = item.title
                txtCurrentEpisode.text = item.currentSeries.toString()

                Glide.with(fragmentActivity)
                    .load(item.imageUriString.toUri())
                    .into(ivAnimePreview)
            }

            binding.apply {
                root.setOnClickListener { onItemClicked.invoke(item) }
                ivMinus.setOnClickListener { onMinusClicked.invoke(item) }
                ivPlus.setOnClickListener { onPlusClicked.invoke(item) }
            }
        }
    }

    // fixme: If we change state of anime, we can have bugs with it's type, if ir were first for example

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

    fun updateList(newList: List<UserWatchingAnime>) {
        val diffUtilCallback = UserWatchingAnimeDiffUtilCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        list.apply {
            clear()
            addAll(newList)
        }
        sortList(sortingCharacteristics, sortingWay)
        diffResult.dispatchUpdatesTo(this)
    }

    fun sortList(sortingCharacteristics: SortingCharacteristics, sortingWay: SortingWay) {
        if (sortingNotChanged(sortingCharacteristics, sortingWay)) return
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
        notifyItemRangeChanged(0, list.size)
    }
}


class UserWatchingAnimeDiffUtilCallback(
    private val oldList: List<UserWatchingAnime>,
    private val newList: List<UserWatchingAnime>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].currentSeries == newList[newItemPosition].currentSeries
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if (oldList[oldItemPosition].id == newList[newItemPosition].id &&
            oldList[oldItemPosition].currentSeries != newList[newItemPosition].currentSeries
        ) {
            return newList[newItemPosition].currentSeries
        }
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}