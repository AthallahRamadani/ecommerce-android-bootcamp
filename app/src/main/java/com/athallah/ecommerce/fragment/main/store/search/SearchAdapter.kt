package com.athallah.ecommerce.fragment.main.store.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.databinding.SearchItemBinding

class SearchAdapter(private val itemClickCallback: (String) -> Unit) :
    ListAdapter<String, SearchAdapter.SearchProductViewHolder>(SearchComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        val binding =
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchProductViewHolder(binding, itemClickCallback)
    }

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class SearchProductViewHolder(
        private val binding: SearchItemBinding,
        private val itemClickCallback: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.tvSearch.text = data
            itemView.setOnClickListener { itemClickCallback(data) }
        }
    }

    object SearchComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}
