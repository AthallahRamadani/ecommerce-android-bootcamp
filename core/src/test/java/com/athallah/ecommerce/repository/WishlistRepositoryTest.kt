package com.athallah.ecommerce.repository

import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.data.repo.WishlistRepositoryImpl
import com.athallah.ecommerce.utils.extension.toWishlist
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistRepositoryTest {

    private lateinit var wishlistRepository: WishlistRepository
    private val wishlistDao: WishlistDao = mock()

    @Before
    fun setUp() {
        wishlistRepository = WishlistRepositoryImpl(
            wishlistDao
        )
    }

    @Test
    fun getWishlistData() = runTest {
        val wishlistEntity = WishlistEntity(
            "",
            "",
            2,
            "",
            2,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2
        )
        whenever(wishlistDao.getData()).thenReturn(flowOf(listOf(wishlistEntity)))
        val expectedData = listOf(wishlistEntity.toWishlist())
        val actualData = wishlistRepository.getWishlistData().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getWishlistDataSize() = runTest {
        whenever(wishlistDao.getDataSize()).thenReturn(flowOf(2))
        val actualData = wishlistRepository.getWishlistDataSize().first()
        assertEquals(2, actualData)
    }

    @Test
    fun checkExistWishlistData() = runTest {
        whenever(wishlistDao.checkExistData(wishlistId = "2")).thenReturn(false)
        val actualData = wishlistRepository.checkExistWishlistData(wishlistId = "2")
        assertEquals(false, actualData)
    }

    @Test
    fun insertWishlist() = runTest {
        val wishlistEntity = WishlistEntity(
            "",
            "",
            2,
            "",
            2,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2
        )
        whenever(wishlistDao.checkExistData(wishlistEntity.productId)).thenReturn(false)
        wishlistRepository.insertWishlist(wishlistEntity.toWishlist())
        verify(wishlistDao).insert(wishlistEntity)
    }

    @Test
    fun deleteWishlist() = runTest {
        val wishlistEntity = WishlistEntity(
            "",
            "",
            2,
            "",
            2,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2
        )
        wishlistRepository.deleteWishlist(wishlistEntity.toWishlist())
        verify(wishlistDao).delete(wishlistEntity)
    }
}
