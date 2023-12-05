package com.athallah.ecommerce.data.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_price") val productPrice: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "store") val store: String,
    @ColumnInfo(name = "sale") val sale: Int,
    @ColumnInfo(name = "product_rating") val productRating: Float,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "total_rating") val totalRating: Int,
    @ColumnInfo(name = "total_satisfaction") val totalSatisfaction: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "total_review") val totalReview: Int,
    @ColumnInfo(name = "variant_name") val variantName: String,
    @ColumnInfo(name = "variant_price") val variantPrice: Int,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "is_checked") val isChecked: Boolean,
)
