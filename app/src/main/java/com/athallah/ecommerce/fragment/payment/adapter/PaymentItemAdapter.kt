package com.athallah.ecommerce.fragment.payment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.databinding.PaymentItemLayoutItemLayoutBinding
import com.athallah.ecommerce.utils.Helper

class PaymentItemAdapter(
    private val itemClickCallback: (Payment.PaymentItem) -> Unit
) : ListAdapter<Payment.PaymentItem, PaymentItemAdapter.PaymentItemViewHolder>(PaymentItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentItemViewHolder {
        val binding =
            PaymentItemLayoutItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PaymentItemViewHolder(binding, itemClickCallback)
    }

    override fun onBindViewHolder(holder: PaymentItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PaymentItemViewHolder(
        private val binding: PaymentItemLayoutItemLayoutBinding,
        private val itemClickCallback: (Payment.PaymentItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Payment.PaymentItem) {
            Log.d("TAs", "bind: $data")
            binding.ivPayment.load(data.image) {
                crossfade(true)
                placeholder(R.drawable.product_image_placeholder)
                error(R.drawable.product_image_placeholder)
            }
            binding.tvPayment.text = data.label
            if (!data.status) {
                with(binding.layoutPaymentSubitem) {
                    alpha = 0.25F
                    isClickable = false
                    isFocusable = false
                    foreground = null
                    setBackgroundColor(
                        Helper.getColorTheme(
                            itemView.context,
                            com.google.android.material.R.attr.colorTertiaryContainer
                        )
                    )
                }
                binding.layoutPaymentSubitem.alpha = 0.25F
            } else {
                itemView.setOnClickListener { itemClickCallback(data) }
            }
        }
    }

    object PaymentItemComparator : DiffUtil.ItemCallback<Payment.PaymentItem>() {
        override fun areItemsTheSame(
            oldItem: Payment.PaymentItem,
            newItem: Payment.PaymentItem
        ): Boolean = oldItem.label == newItem.label

        override fun areContentsTheSame(
            oldItem: Payment.PaymentItem,
            newItem: Payment.PaymentItem
        ): Boolean = oldItem == newItem
    }
}
