package ru.alexp0111.onigoing.ui.lists.page

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.alexp0111.onigoing.R
import ru.alexp0111.onigoing.database.user_watching_anime.data.UserWatchingAnime
import ru.alexp0111.onigoing.databinding.ItemListAnimeBinding
import ru.alexp0111.onigoing.databinding.ItemListAnimeBottomBinding
import ru.alexp0111.onigoing.ui.lists.SortingCharacteristics
import ru.alexp0111.onigoing.ui.lists.SortingWay

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
    private val onMinusClicked: (UserWatchingAnime) -> Unit,
    private val onPlusClicked: (UserWatchingAnime) -> Unit,
    private val onMarkSelected: (UserWatchingAnime, Int) -> Unit,
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
                glMark.children.apply {
                    forEachIndexed { index, view ->
                        paintView(view, resolveColor(index, item))
                        setUpListener(view, index, item)
                    }
                }
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
                glMark.children.apply {
                    forEachIndexed { index, view ->
                        paintView(view, resolveColor(index, item))
                        setUpListener(view, index, item)
                    }
                }
            }
        }
    }

    private fun resolveColor(index: Int, item: UserWatchingAnime): StarColors {
        return if (index + 1 <= item.mark) {
            StarColors.MAIN_ACCENT
        } else {
            StarColors.WHITE
        }
    }

    private fun setUpListener(view: View, viewIndex: Int, item: UserWatchingAnime) {
        view.setOnClickListener {
            onMarkSelected.invoke(item, viewIndex + 1)
        }
    }

    private fun paintView(view: View, starColor: StarColors) {
        val typedValue = TypedValue().apply {
            fragmentActivity.theme.resolveAttribute(
                R.attr.main_accent_color,
                this,
                true,
            )
        }
        val color = when (starColor) {
            StarColors.MAIN_ACCENT -> typedValue.data
            StarColors.WHITE -> fragmentActivity.getColor(R.color.white)
        }
        (view as ImageView).setColorFilter(color)
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

    fun sortList(
        incomingList: List<UserWatchingAnime>? = null,
        sortingCharacteristics: SortingCharacteristics,
        sortingWay: SortingWay
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