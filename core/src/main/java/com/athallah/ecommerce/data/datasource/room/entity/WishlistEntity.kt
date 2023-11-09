package com.athallah.ecommerce.data.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wishlist")
data class WishlistEntity (
    @PrimaryKey
    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_price") val productPrice: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "store") val store: String,
    @ColumnInfo(name = "sale") val sale: Int,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "product_rating") val productRating: Float,
    @ColumnInfo(name = "product_variant") val productVariant: String
)