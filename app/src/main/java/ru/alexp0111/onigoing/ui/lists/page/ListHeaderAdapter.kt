package ru.alexp0111.onigoing.ui.lists.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexp0111.onigoing.databinding.ItemListHeaderBinding

class ListHeaderAdapter :
    RecyclerView.Adapter<ListHeaderAdapter.ListHeaderAdapterViewHolder>() {

    private val pages = intArrayOf(
        Pages.VIEWED.ordinal,
        Pages.ACTUAL.ordinal,
        Pages.PLANNED.ordinal,
        Pages.STOPPED.ordinal,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHeaderAdapterViewHolder {
        val itemView = ItemListHeaderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHeaderAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: ListHeaderAdapterViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ListHeaderAdapterViewHolder(val binding: ItemListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(index: Int) {
            binding.apply {
                txtHeader.text = Pages.from(index).description
            }
        }
    }
}