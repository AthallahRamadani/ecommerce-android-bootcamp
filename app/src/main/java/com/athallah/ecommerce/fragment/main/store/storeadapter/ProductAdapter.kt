package com.athallah.ecommerce.fragment.main.store.storeadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Product
import com.athallah.ecommerce.databinding.ProductGridLayoutBinding
import com.athallah.ecommerce.databinding.ProductListLayoutBinding
import com.athallah.ecommerce.utils.toCurrencyFormat

class ProductAdapter(
    private val itemClickCallback: (Product) -> Unit
) :
    PagingDataAdapter<Product, RecyclerView.ViewHolder>(ProductComparator) {

    var viewType = ONE_COLUMN_VIEW_TYPE

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as ProductViewHolder<*>).bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ONE_COLUMN_VIEW_TYPE) {
            val binding =
                ProductListLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ProductViewHolder(binding, viewType, itemClickCallback)
        } else {
            val binding =
                ProductGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ProductViewHolder(binding, viewType, itemClickCallback)
        }
    }

    class ProductViewHolder<T : ViewBinding>(
        private val binding: T,
        private val viewType: Int,
        private val itemClickCallback: (Product) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            if (viewType == ONE_COLUMN_VIEW_TYPE) {
                with(binding as ProductListLayoutBinding) {
                    ivProduct.load(data.image) {
                        crossfade(true)
                        placeholder(R.drawable.product_image)
                        error(R.drawable.product_image)
                    }
                    tvProductName.text = data.productName
                    tvProductPrice.text = data.productPrice.toCurrencyFormat()
                    tvProductStore.text = data.store
                    tvProductRatingAndSales.text =
                        itemView.resources.getString(
                            R.string.product_rating_and_sales,
                            data.productRating,
                            data.sale
                        )
                }
            } else {
                with(binding as ProductGridLayoutBinding) {
                    ivProductImage.load(data.image) {
                        crossfade(true)
                        placeholder(R.drawable.product_image)
                        error(R.drawable.product_image)
                    }
                    tvProductName.text = data.productName
                    tvProductPrice.text = data.productPrice.toCurrencyFormat()
                    tvProductStore.text = data.store
                    tvProductRatingAndSales.text =
                        itemView.resources.getString(
                            R.string.product_rating_and_sales,
                            data.productRating,
                            data.sale
                        )
                }
            }
            itemView.setOnClickListener { itemClickCallback(data) }
        }
    }

    object ProductComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem


    }

    companion object {
        const val ONE_COLUMN_VIEW_TYPE = 1
        const val MORE_COLUMN_VIEW_TYPE = 2
    }


}


