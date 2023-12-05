package com.athallah.ecommerce.fragment.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.DetailItemImageBinding

class DetailAdapter :
    ListAdapter<String, DetailAdapter.DetailViewHolder>(DetailImageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding =
            DetailItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DetailViewHolder(private val binding: DetailItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.ivDetailImage.load(data) {
                crossfade(true)
                placeholder(R.drawable.product_image_placeholder)
                error(R.drawable.product_image_placeholder)
            }
        }
    }

    class DetailImageDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}
