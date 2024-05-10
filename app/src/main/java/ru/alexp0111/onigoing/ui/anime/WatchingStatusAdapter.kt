package ru.alexp0111.onigoing.ui.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexp0111.onigoing.databinding.ItemWatchingStateBinding
import ru.alexp0111.onigoing.ui.lists.page.Pages

class WatchingStatusAdapter :
    RecyclerView.Adapter<WatchingStatusAdapter.WatchingStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchingStatusViewHolder {
        val itemView = ItemWatchingStateBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchingStatusViewHolder(itemView)
    }

    override fun getItemCount(): Int = Pages.entries.size

    override fun onBindViewHolder(holder: WatchingStatusViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class WatchingStatusViewHolder(val binding: ItemWatchingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(index: Int) {
            binding.apply {
                txtStatus.text = Pages.from(index).description
            }
        }
    }
}