package ru.alexp0111.onigoing.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.anilist.data.Search
import ru.alexp0111.onigoing.databinding.ItemAnimeSearchBinding

class SearchAnimeAdapter(
    private val activity: FragmentActivity,
    private val onItemClicked: (Search) -> Unit,
) : RecyclerView.Adapter<SearchAnimeAdapter.SearchAnimeViewHolder>() {

    private var list: MutableList<Search> = mutableListOf()

    inner class SearchAnimeViewHolder(
        private val binding: ItemAnimeSearchBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            binding.apply {
                txtTitleName.text = item.title.toString()
                txtNumOfSeries.text = if (item.episodes == null) {
                    "Ongoing"
                } else {
                    activity.resources.getQuantityString(
                        R.plurals.plural_episodes,
                        item.episodes,
                        item.episodes
                    )
                }
                item.averageScore?.let {
                    ivStar.isVisible = true
                    txtMark.text = it.toString()
                }
                Glide.with(activity).load(item.coverImage?.toUri()).into(ivPreview)
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAnimeViewHolder {
        val itemView = ItemAnimeSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchAnimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchAnimeViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(listOfResults: List<Search>) {
        list = listOfResults.toMutableList()
        notifyDataSetChanged()
    }
}