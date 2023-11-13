package com.athallah.ecommerce.fragment.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.databinding.CartItemLayoutBinding
import com.athallah.ecommerce.utils.Helper
import com.athallah.ecommerce.utils.toCurrencyFormat

class CartAdapter(
    private val callback: CartCallback
) : ListAdapter<Cart, CartAdapter.CartViewHolder>(CartComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartViewHolder(
        private val binding: CartItemLayoutBinding,
        private val callback: CartCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Cart) {
            with(binding) {
                ivCartImage.load(data.image) {
                    crossfade(true)
                    placeholder(R.drawable.product_image_placeholder)
                    error(R.drawable.product_image_placeholder)
                }
                tvCartName.text = data.productName
                tvCartVariant.text = data.variantName
                tvCartStock.text = getStockText(data.stock)
                tvCartPrice.text = data.productPrice.plus(data.variantPrice).toCurrencyFormat()
                tvCartQuantity.text = data.quantity.toString()
                checkboxItemCart.isChecked = data.isChecked

                btnDeleteCart.setOnClickListener { callback.deleteItemCallback(data) }
                btnPlusCart.setOnClickListener { callback.addQuantityCallback(data) }
                btnMinusCart.setOnClickListener { callback.removeQuantityCallback(data) }
                checkboxItemCart.setOnClickListener {
                    callback.checkItemCallback(data, !data.isChecked)
                }
            }
            itemView.setOnClickListener { callback.itemClickCallback(data.productId) }
        }

        private fun getStockText(stock: Int): String {
            return if (stock >= 10) {
                binding.tvCartStock.setTextColor(
                    Helper.getColorTheme(
                        itemView.context,
                        com.google.android.material.R.attr.colorOnSurface
                    )
                )
                itemView.context.getString(R.string.total_stock, stock)
            } else {
                binding.tvCartStock.setTextColor(
                    Helper.getColorTheme(
                        itemView.context,
                        com.google.android.material.R.attr.colorError
                    )
                )
                itemView.context.getString(R.string.remaining_stock, stock)
            }
        }
    }

    interface CartCallback {
        fun itemClickCallback(productId: String)
        fun deleteItemCallback(cart: Cart)
        fun addQuantityCallback(cart: Cart)
        fun removeQuantityCallback(cart: Cart)
        fun checkItemCallback(cart: Cart, isCheck: Boolean)
    }

    object CartComparator : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean =
            oldItem == newItem
    }


}