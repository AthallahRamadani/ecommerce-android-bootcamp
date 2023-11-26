package com.athallah.ecommerce

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.athallah.ecommerce.data.datasource.room.AppDatabase
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import com.athallah.ecommerce.data.datasource.room.entity.NotificationEntity
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomTest {
    private lateinit var cartDao: CartDao
    private lateinit var notificationDao: NotificationDao
    private lateinit var wishlistDao: WishlistDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        cartDao = db.cartDao()
        notificationDao = db.notificationDao()
        wishlistDao = db.wishlistDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    //cartDao
    @Test
    fun testGetDataCart() = runTest {
        val cart = CartEntity(
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
            2,
            2,
            true
        )
        cartDao.insert(cart)
        val actualData = cartDao.getData().first()
        val expectedData = listOf(cart)

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testGetDetailDataCart() = runTest {
        val cart = CartEntity(
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
            2,
            2,
            true
        )
        cartDao.insert(cart)
        val actualData = cartDao.getDetailData(cart.productId).first()

        assertEquals(cart, actualData)
    }

    @Test
    fun testGetDataSizeCart() = runTest {
        val array = ArrayList<CartEntity>()
        for (i in 1..6) {
            val cart = CartEntity(
                "$i",
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
                2,
                2,
                true
            )
            array.add(cart)
            cartDao.insert(cart)
        }

        val actualData = cartDao.getDataSize().first()
        val expectedData = array.size

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testCheckExistDataCart() = runTest {
        val cart = CartEntity(
            "",
            "",
            2,
            "",
            0,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        cartDao.insert(cart)
        val actualData = cartDao.checkExistData(cart.productId)
        val expectedData = true

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testUpdateCart() = runTest {
        val cart = CartEntity(
            "",
            "",
            2,
            "",
            0,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        cartDao.insert(cart)
        cartDao.update(
            cart.copy(
                quantity = 100
            )
        )
        val actualData = cartDao.getData().first()
        val expectedData = listOf(
            cart.copy(
                quantity = 100
            )
        )

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testDeleteCart() = runTest {
        val array = ArrayList<CartEntity>()
        for (i in 1..6) {
            val cart = CartEntity(
                "$i",
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
                2,
                2,
                true
            )
            array.add(cart)
            cartDao.insert(cart)
        }

        cartDao.delete(array[2])

        val actualData = cartDao.getDataSize().first()
        val expectedData = array.size - 1

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testClearTableCart() = runTest {
        val array = ArrayList<CartEntity>()
        for (i in 1..6) {
            val cart = CartEntity(
                "$i",
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
                2,
                2,
                true
            )
            array.add(cart)
            cartDao.insert(cart)
        }

        cartDao.clearTable()

        val actualData = cartDao.getDataSize().first()
        val expectedData = 0

        assertEquals(expectedData, actualData)
    }

    //notificationDao
    @Test
    fun testGetDataNotification() = runTest {
        val notification = NotificationEntity(
            1,
            "",
            "",
            "",
            "",
            "",
            "",
            true
        )
        notificationDao.insert(notification)
        val actualData = notificationDao.getData().first()
        val expectedData = listOf(notification)

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testGetDataSizeNotification() = runTest {
        val array = ArrayList<NotificationEntity>()
        for (i in 1..6) {
            val notification = NotificationEntity(
                i,
                "",
                "",
                "",
                "",
                "",
                "",
                false
            )
            array.add(notification)
            notificationDao.insert(notification)
        }

        val actualData = notificationDao.getDataSize().first()
        val expectedData = array.size

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testUpdateNotification() = runTest {
        val notification = NotificationEntity(
            1,
            "",
            "",
            "",
            "",
            "",
            "",
            false
        )
        notificationDao.insert(notification)
        notificationDao.update(
            notification.copy(
                isRead = true
            )
        )
        val actualData = notificationDao.getData().first()
        val expectedData = listOf(
            notification.copy(
                isRead = true
            )
        )

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testClearTableNotification() = runTest {
        val array = ArrayList<NotificationEntity>()
        for (i in 1..6) {
            val notification = NotificationEntity(
                i,
                "",
                "",
                "",
                "",
                "",
                "",
                false
            )
            array.add(notification)
            notificationDao.insert(notification)
        }

        notificationDao.clearTable()

        val actualData = notificationDao.getDataSize().first()
        val expectedData = 0

        assertEquals(expectedData, actualData)
    }


    //wishlistDao
    @Test
    fun testGetDataWishlist() = runTest {
        val wishlist = WishlistEntity(
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
            2,
        )
        wishlistDao.insert(wishlist)
        val actualData = wishlistDao.getData().first()
        val expectedData = listOf(wishlist)

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testGetDataSizeWishlist() = runTest {
        val array = ArrayList<WishlistEntity>()
        for (i in 1..6) {
            val wishlist = WishlistEntity(
                "$i",
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
                2,
            )
            array.add(wishlist)
            wishlistDao.insert(wishlist)
        }

        val actualData = wishlistDao.getDataSize().first()
        val expectedData = array.size

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testCheckExistDataWishlist() = runTest {
        val wishlist = WishlistEntity(
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
            2,
        )
        wishlistDao.insert(wishlist)
        val actualData = wishlistDao.checkExistData(wishlist.productId)
        val expectedData = true

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testDeleteWishlist() = runTest {
        val array = ArrayList<WishlistEntity>()
        for (i in 1..6) {
            val wishlist = WishlistEntity(
                "$i",
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
                2,
            )
            array.add(wishlist)
            wishlistDao.insert(wishlist)
        }

        wishlistDao.delete(array[2])

        val actualData = wishlistDao.getDataSize().first()
        val expectedData = array.size - 1

        assertEquals(expectedData, actualData)
    }

    @Test
    fun testClearTableWishlist() = runTest {
        val array = ArrayList<WishlistEntity>()
        for (i in 1..6) {
            val wishlist = WishlistEntity(
                "$i",
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
                2,
            )
            array.add(wishlist)
            wishlistDao.insert(wishlist)
        }

        wishlistDao.clearTable()

        val actualData = wishlistDao.getDataSize().first()
        val expectedData = 0

        assertEquals(expectedData, actualData)
    }






    


}