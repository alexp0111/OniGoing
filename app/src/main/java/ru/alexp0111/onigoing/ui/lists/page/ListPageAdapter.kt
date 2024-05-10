package ru.alexp0111.onigoing.ui.lists.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.ItemListAnimeBinding

class ListPageAdapter(
    private val fragmentActivity: FragmentActivity,
    private val onItemClicked: (UserWatchingAnime) -> Unit,
) : RecyclerView.Adapter<ListPageAdapter.ListPageViewHolder>() {

    private val list: MutableList<UserWatchingAnime> = mutableListOf()

    inner class ListPageViewHolder(
        private val binding: ItemListAnimeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserWatchingAnime) {
            binding.apply {
                txtAnimeTitle.text = item.title
                txtCurrentEpisode.text = item.currentSeries.toString()

                Glide.with(fragmentActivity)
                    .load(item.imageUriString.toUri())
                    .into(ivAnimePreview)
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageViewHolder {
        val itemView = ItemListAnimeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListPageViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(newList: List<UserWatchingAnime>) {
        val diffUtilCallback = HistoryDiffUtilCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        list.apply {
            clear()
            addAll(newList)
        }
    }
}


class HistoryDiffUtilCallback(oldList: List<UserWatchingAnime>, newList: List<UserWatchingAnime>) :
    DiffUtil.Callback() {
    private val oldList: List<UserWatchingAnime>
    private val newList: List<UserWatchingAnime>

    init {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: UserWatchingAnime = oldList[oldItemPosition]
        val newProduct: UserWatchingAnime = newList[newItemPosition]
        return oldProduct === newProduct
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: UserWatchingAnime = oldList[oldItemPosition]
        val newProduct: UserWatchingAnime = newList[newItemPosition]
        return oldProduct == newProduct
    }
}