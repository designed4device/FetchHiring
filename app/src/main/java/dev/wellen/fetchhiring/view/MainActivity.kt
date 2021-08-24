package dev.wellen.fetchhiring.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.wellen.fetchhiring.databinding.ActivityMainBinding
import dev.wellen.fetchhiring.databinding.ListItemBinding
import dev.wellen.fetchhiring.databinding.ListSectionHeaderBinding
import dev.wellen.fetchhiring.viewmodel.ListItem
import dev.wellen.fetchhiring.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerViewAdapter = RecyclerViewAdapter()

        binding = ActivityMainBinding.inflate(layoutInflater)
            .also {
                setContentView(it.root)
                it.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerViewAdapter
                }
            }

        viewModel.sections.observe(this) {
            recyclerViewAdapter.setItems(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ListItem> = emptyList()

    fun setItems(items: List<ListItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> SectionHeaderViewHolder.from(parent)
            else -> ItemViewHolder.from(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is SectionHeaderViewHolder -> (item as? ListItem.SectionHeader)?.let { holder.bind(it) }
            is ItemViewHolder -> (item as? ListItem.Item)?.let { holder.bind(it) }
        }
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is ListItem.SectionHeader -> 0
        is ListItem.Item -> 1
    }
}

class SectionHeaderViewHolder private constructor(private val binding: ListSectionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sectionHeader: ListItem.SectionHeader) {
        binding.listId.text = sectionHeader.id.toString()
    }

    companion object {
        fun from(parent: ViewGroup): SectionHeaderViewHolder = SectionHeaderViewHolder(
            ListSectionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class ItemViewHolder private constructor(private val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem.Item) {
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

