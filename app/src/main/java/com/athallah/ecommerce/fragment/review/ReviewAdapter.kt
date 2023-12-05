package com.athallah.ecommerce.fragment.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Review
import com.athallah.ecommerce.databinding.ReviewItemLayoutBinding

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewImageDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            ReviewItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewViewHolder(private val binding: ReviewItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Review) {
            binding.civImage.load(data.userImage) {
                crossfade(true)
                placeholder(R.drawable.product_image_placeholder)
                error(R.drawable.product_image_placeholder)
            }
            binding.tvName.text = data.userName
            binding.tvReview.text = data.userReview
            binding.ratingBar.rating = data.userRating.toFloat()
        }
    }

    class ReviewImageDiffUtil : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
            oldItem.userName == newItem.userName

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
            oldItem == newItem
    }
}
