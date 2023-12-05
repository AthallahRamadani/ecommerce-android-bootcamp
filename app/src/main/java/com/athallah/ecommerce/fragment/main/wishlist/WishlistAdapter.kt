package com.athallah.ecommerce.fragment.main.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.databinding.ProductWishlistGridLayoutBinding
import com.athallah.ecommerce.databinding.ProductWishlistListLayoutBinding
import com.athallah.ecommerce.utils.toCurrencyFormat

class WishlistAdapter(
    private val callback: WishlistCallback
) : ListAdapter<Wishlist, WishlistAdapter.WishlistViewHolder<ViewBinding>>(WishlistComparator) {

    var viewType = ONE_COLUMN_VIEW_TYPE

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistViewHolder<ViewBinding> {
        if (viewType == ONE_COLUMN_VIEW_TYPE) {
            val binding =
                ProductWishlistListLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return WishlistViewHolder(binding, viewType, callback)
        } else {
            val binding =
                ProductWishlistGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return WishlistViewHolder(binding, viewType, callback)
        }
    }

    override fun onBindViewHolder(
        holder: WishlistViewHolder<ViewBinding>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class WishlistViewHolder<T : ViewBinding>(
        private val binding: T,
        private val viewType: Int,
        private val callback: WishlistCallback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Wishlist) {
            if (viewType == ONE_COLUMN_VIEW_TYPE) {
                with(binding as ProductWishlistListLayoutBinding) {
                    bindView(
                        data,
                        ivProduct,
                        tvProductName,
                        tvProductPrice,
                        tvProductStore,
                        tvProductRatingAndSales,
                        btProductDeleteWishlist,
                        btProductAddCart
                    )
                }
            } else {
                with(binding as ProductWishlistGridLayoutBinding) {
                    bindView(
                        data,
                        ivProductImage,
                        tvProductName,
                        tvProductPrice,
                        tvProductStore,
                        tvProductRatingAndSales,
                        btProductDeleteWishlist,
                        btProductAddCart
                    )
                }
            }
            itemView.setOnClickListener { callback.itemClickCallback(data.productId) }
        }

        private fun bindView(
            data: Wishlist,
            productImage: ImageView,
            productName: TextView,
            productPrice: TextView,
            productStore: TextView,
            productRatingAndSales: TextView,
            buttonDelete: Button,
            buttonAddCart: Button
        ) {
            productImage.load(data.image) {
                crossfade(true)
                placeholder(R.drawable.product_image_placeholder)
                error(R.drawable.product_image_placeholder)
            }
            productName.text = data.productName
            productPrice.text = data.productPrice.plus(data.variantPrice).toCurrencyFormat()
            productStore.text = data.store
            productRatingAndSales.text =
                itemView.resources.getString(
                    R.string.product_rating_and_sales,
                    data.productRating,
                    data.sale
                )
            buttonDelete.setOnClickListener { callback.deleteCallback(data) }
            buttonAddCart.setOnClickListener { callback.addCartCallback(data) }
        }
    }

    interface WishlistCallback {
        fun itemClickCallback(productId: String)
        fun deleteCallback(wishlist: Wishlist)
        fun addCartCallback(wishlist: Wishlist)
    }

    object WishlistComparator : DiffUtil.ItemCallback<Wishlist>() {
        override fun areItemsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean =
            oldItem == newItem
    }

    companion object {
        const val ONE_COLUMN_VIEW_TYPE = 1
        const val MORE_COLUMN_VIEW_TYPE = 2
    }
}
