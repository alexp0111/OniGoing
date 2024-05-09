package ru.alexp0111.onigoing.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.anilist.data.Search
import ru.alexp0111.onigoing.databinding.ItemAnimeSearchBinding
import ru.alexp0111.onigoing.utils.SharedPreferenceController

@AssistedFactory
interface SearchAnimeAdapterFactory {
    fun create(activity: FragmentActivity, onItemClicked: (Search) -> Unit): SearchAnimeAdapter
}

class SearchAnimeAdapter @AssistedInject constructor(
    @Assisted private val activity: FragmentActivity,
    @Assisted private val onItemClicked: (Search) -> Unit,
    private val sharedPreferenceController: SharedPreferenceController,
) : RecyclerView.Adapter<SearchAnimeAdapter.SearchAnimeViewHolder>() {

    private var list: MutableList<Search> = mutableListOf()

    inner class SearchAnimeViewHolder(
        private val binding: ItemAnimeSearchBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            binding.apply {
                txtTitleName.text = item.title ?: "No data"
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
                item.coverImage?.toUri()?.let {
                    Glide.with(activity).load(it).into(ivPreview)
                }
            }

            binding.root.setOnClickListener {
                item.title?.let { sharedPreferenceController.insertNewHistoryElement(it) }
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