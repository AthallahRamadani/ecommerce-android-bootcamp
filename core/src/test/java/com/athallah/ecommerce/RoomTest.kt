package com.athallah.ecommerce

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.athallah.ecommerce.data.datasource.room.AppDatabase
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
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

    @Test
    fun testGetData() = runTest {
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
    fun testGetDetailData() = runTest {
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
    fun testGetDataSize() = runTest {
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
    fun testCheckExistData() = runTest {
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
    fun testUpdate() = runTest {
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
    fun testDelete() = runTest {
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
    fun testClearTable() = runTest {
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




}