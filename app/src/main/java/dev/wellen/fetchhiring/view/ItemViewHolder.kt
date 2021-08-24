package dev.wellen.fetchhiring.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.wellen.fetchhiring.databinding.ListItemBinding
import dev.wellen.fetchhiring.model.Item

class ItemViewHolder private constructor(private val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.listId.text = item.listId.toString()
        binding.id.text = item.id.toString()
        binding.name.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): ItemViewHolder = ItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}