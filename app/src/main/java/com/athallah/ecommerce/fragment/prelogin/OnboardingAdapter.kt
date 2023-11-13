package com.athallah.ecommerce.fragment.prelogin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.databinding.ItemPageBinding

class OnboardingAdapter(private var images: List<Int>) :
    RecyclerView.Adapter<OnboardingAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val itemImage = binding.ivImage
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(
            ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
