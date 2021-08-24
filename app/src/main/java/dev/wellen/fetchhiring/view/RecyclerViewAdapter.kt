package dev.wellen.fetchhiring.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.wellen.fetchhiring.model.Item

class RecyclerViewAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private var items: List<Item> = emptyList()

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}