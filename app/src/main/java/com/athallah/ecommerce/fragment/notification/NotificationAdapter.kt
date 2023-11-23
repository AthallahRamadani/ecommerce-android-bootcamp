package com.athallah.ecommerce.fragment.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.databinding.NotificationItemLayoutBinding
import com.athallah.ecommerce.utils.Helper

class NotificationAdapter(
    private val itemClickedCallback: (Notification) -> Unit
) :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(NotificationComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            NotificationItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NotificationViewHolder(binding, itemClickedCallback)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(
        private val binding: NotificationItemLayoutBinding,
        private val itemClickedCallback: (Notification) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notification) {
            with(binding) {
                ivNotificationImage.load(data.image) {
                    crossfade(true)
                    placeholder(R.drawable.product_image_placeholder)
                    error(R.drawable.product_image_placeholder)
                }
                tvNotificationType.text = data.type
                tvNotificationTitle.text = data.title
                tvNotificationBody.text = data.body
                tvNotificationDatetime.text =
                    itemView.context.getString(R.string.date_time, data.date, data.time)

                if (!data.isRead) {
                    layoutItem.setBackgroundColor(
                        Helper.getColorTheme(
                            itemView.context,
                            com.google.android.material.R.attr.colorOutlineVariant
                        )
                    )
                } else {
                    layoutItem.setBackgroundColor(
                        Helper.getColorTheme(
                            itemView.context,
                            com.google.android.material.R.attr.colorSurface
                        )
                    )
                    layoutItem.isClickable = false
                    layoutItem.isFocusable = false
                    layoutItem.foreground = null
                }
            }
            itemView.setOnClickListener { itemClickedCallback(data) }
        }
    }
    object NotificationComparator : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean =
            oldItem.title == newItem.title &&
                    oldItem.body == newItem.body &&
                    oldItem.date == newItem.date &&
                    oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean =
            oldItem == newItem
    }
}