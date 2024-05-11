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
        diffResult.dispatchUpdatesTo(this)
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