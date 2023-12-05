package com.athallah.ecommerce.fragment.main.transaction

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Transaction
import com.athallah.ecommerce.databinding.TransactionItemLayoutBinding
import com.athallah.ecommerce.utils.toCurrencyFormat

class TransactionAdapter(private val onItemClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionComparator) {
    object TransactionComparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            oldItem.invoiceId == newItem.invoiceId

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
            oldItem == newItem
    }

    class TransactionViewHolder(
        private val binding: TransactionItemLayoutBinding,
        private val onItemClick: (Transaction) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Transaction) {
            with(binding) {
                tvDate.text = data.date
                ivProduct.load(data.image) {
                    crossfade(true)
                    placeholder(R.drawable.product_image_placeholder)
                    error(R.drawable.product_image_placeholder)
                }
                tvProduct.text = data.name
                Log.d("udin", "bind: ${data.name}")
                tvTotalItem.text =
                    itemView.context.getString(R.string.total_item, data.items.size)
                tvTransactionTotalItem.text = data.total.toCurrencyFormat()
                if (data.rating == 0 || data.review.isEmpty()) {
                    btnReview.setOnClickListener { onItemClick(data) }
                } else {
                    btnReview.isVisible = false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
