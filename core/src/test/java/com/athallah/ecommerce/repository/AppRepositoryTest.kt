package com.athallah.ecommerce.repository

import com.athallah.ecommerce.data.datasource.firebase.FirebaseSubscribe
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.AppRepositoryImpl
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AppRepositoryTest {

    private lateinit var appRepository: AppRepository
    private val sharedPref: UserDataStore = mock()
    private val wishlistDao: WishlistDao = mock()
    private val notificationDao: NotificationDao = mock()
    private val cartDao: CartDao = mock()
    private val firebaseSubscribe: FirebaseSubscribe = mock()


    @Before
    fun setUp() {
        appRepository =
            AppRepositoryImpl(sharedPref, wishlistDao, notificationDao, cartDao, firebaseSubscribe)
    }

    @Test
    fun getRefToken() = runTest {
        whenever(sharedPref.getRefToken()).thenReturn(flowOf("123"))
        val actualData = appRepository.getRefToken().first()

        assertEquals("123", actualData)

    }

    @Test
    fun setRefToken() = runTest {
        appRepository.setRefToken("222")
        verify(sharedPref).setRefToken("222")
    }

    @Test
    fun getAccToken() = runTest {
        whenever(sharedPref.getAccToken()).thenReturn(flowOf("123"))
        val actualData = appRepository.getAccToken().first()

        assertEquals("123", actualData)

    }

    @Test
    fun setAccToken() = runTest {
        appRepository.setAccToken("222")
        verify(sharedPref).setAccToken("222")
    }

    @Test
    fun getOnboard() = runTest {
        whenever(sharedPref.getIsOnboard()).thenReturn(flowOf(true))
        val actualData = appRepository.getIsOnboard().first()

        assertEquals(true, actualData)
    }

    @Test
    fun setOnboard() = runTest {
        appRepository.setIsOnBoard(true)
        verify(sharedPref).setIsOnBoard(true)
    }

    @Test
    fun getUsername() = runTest {
        whenever(sharedPref.getUsername()).thenReturn(flowOf("Udin Markotop"))
        val actualData = appRepository.getUsername().first()

        assertEquals("Udin Markotop", actualData)
    }

    @Test
    fun setUsername() = runTest {
        appRepository.setUsername("true")
        verify(sharedPref).setUsername("true")
    }

    @Test
    fun getIsLogin() = runTest {
        whenever(sharedPref.getIsLogin()).thenReturn(flowOf(true))
        val actualData = appRepository.getIsLogin().first()

        assertEquals(true, actualData)
    }

    @Test
    fun setIsLogin() = runTest {
        appRepository.setIsLogin(true)
        verify(sharedPref).setIsLogin(true)
    }

    @Test
    fun logout() = runTest {
        appRepository.logout()
        verify(wishlistDao).clearTable()
        verify(cartDao).clearTable()
        verify(notificationDao).clearTable()
        verify(sharedPref).clearAllDataSession()
        verify(firebaseSubscribe).unsubscribe()
    }

    @Test
    fun checkUserAuthorization() = runTest {
        whenever(sharedPref.checkUserAuthorization()).thenReturn(flowOf(true))
        val actualData = appRepository.checkUserAuthorization().first()

        assertEquals(true, actualData)
    }

    @Test
    fun setUserAuthorization() = runTest {
        appRepository.setUserAuthorization(true)
        verify(sharedPref).setUserAuthorization(true)
    }


}