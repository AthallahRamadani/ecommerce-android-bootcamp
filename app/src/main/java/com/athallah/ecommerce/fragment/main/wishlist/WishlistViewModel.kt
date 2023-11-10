package com.athallah.ecommerce.fragment.main.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.datasource.api.model.Wishlist
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    var recyclerViewType = WishlistAdapter.ONE_COLUMN_VIEW_TYPE
    val wishlistData: Flow<List<Wishlist>> = wishlistRepository.getWishlistData()

    fun deleteWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            wishlistRepository.deleteWishlist(wishlist)
        }
    }

}