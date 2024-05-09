package ru.alexp0111.onigoing.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.alexp0111.onigoing.databinding.ItemSearchHistoryBinding
import ru.alexp0111.onigoing.utils.SharedPreferenceController


@AssistedFactory
fun interface SearchHistoryAdapterFactory {
    operator fun invoke(onItemClicked: (String) -> Unit): SearchHistoryAdapter
}

class SearchHistoryAdapter @AssistedInject constructor(
    private val controller: SharedPreferenceController,
    @Assisted private val onItemClicked: (String) -> Unit,
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    private val list: MutableList<String> by lazy {
        controller.getSearchHistory().historyList
    }

    inner class SearchHistoryViewHolder(
        private val binding: ItemSearchHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                txtSearchElement.text = item
            }

            binding.btnRemove.setOnClickListener {
                controller.removeHistoryElementWithIndex(list.indexOf(item))
                updateList()
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val itemView = ItemSearchHistoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList() {
        val newList = controller.getSearchHistory().historyList
        val diffUtilCallback = HistoryDiffUtilCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
        list.apply {
            clear()
            addAll(newList)
        }
    }
}


class HistoryDiffUtilCallback(oldList: List<String>, newList: List<String>) :
    DiffUtil.Callback() {
    private val oldList: List<String>
    private val newList: List<String>

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
        val oldProduct: String = oldList[oldItemPosition]
        val newProduct: String = newList[newItemPosition]
        return oldProduct === newProduct
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: String = oldList[oldItemPosition]
        val newProduct: String = newList[newItemPosition]
        return oldProduct == newProduct
    }
}