package com.athallah.ecommerce.fragment.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.databinding.CheckoutItemLayoutBinding
import com.athallah.ecommerce.fragment.cart.CartAdapter
import com.athallah.ecommerce.utils.Helper
import com.athallah.ecommerce.utils.toCurrencyFormat

class CheckoutAdapter(
    private val callback: CartCallback
) : ListAdapter<Cart, CheckoutAdapter.CheckoutViewHolder>(CartAdapter.CartComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.CheckoutViewHolder {
        val binding =
            CheckoutItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface CartCallback {
        fun itemClickCallback(productId: String)
        fun addQuantityCallback(cart: Cart)
        fun removeQuantityCallback(cart: Cart)
    }

    class CheckoutViewHolder(
        private val binding: CheckoutItemLayoutBinding,
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

                btnPlusCart.setOnClickListener { callback.addQuantityCallback(data) }
                btnMinusCart.setOnClickListener { callback.removeQuantityCallback(data) }
            }
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
}
