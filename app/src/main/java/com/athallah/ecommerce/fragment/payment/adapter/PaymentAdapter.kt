package com.athallah.ecommerce.fragment.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.databinding.PaymentItemLayoutBinding

class PaymentAdapter(
    private val subitemClickCallback: (Payment.PaymentItem) -> Unit
) : ListAdapter<Payment, PaymentAdapter.PaymentViewHolder>(PaymentComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentViewHolder {
        val binding =
            PaymentItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding, subitemClickCallback)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PaymentViewHolder(
        private val binding: PaymentItemLayoutBinding,
        private val subitemClickCallback: (Payment.PaymentItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val paymentItemAdapter = PaymentItemAdapter(subitemClickCallback)

        fun bind(data: Payment) {
            paymentItemAdapter.submitList(data.item)
            binding.tvPaymentTitle.text = data.title
            binding.rvPaymentItemLayout.apply {
                adapter = paymentItemAdapter
                layoutManager = object : LinearLayoutManager(itemView.context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
        }
    }

    object PaymentComparator : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem == newItem
    }
}
