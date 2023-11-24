package com.athallah.ecommerce

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.di.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class UserDataStoreTest {

    private lateinit var userDataStore: UserDataStore

    @Before
    fun setUp() {
        userDataStore =
            UserDataStore(ApplicationProvider.getApplicationContext<Context>().dataStore)
    }

    @Test
    fun testGetRefToken() = runTest {
        userDataStore.setRefToken("ABC")
        val actualData = userDataStore.getRefToken().first()

        assertEquals("ABC", actualData)
    }

    @Test
    fun testGetAccToken() = runTest {
        userDataStore.setAccToken("ABC")
        val actualData = userDataStore.getAccToken().first()

        assertEquals("ABC", actualData)
    }

    @Test
    fun testGetOnboard() = runTest {
        userDataStore.setIsOnBoard(true)
        val actualData = userDataStore.getIsOnboard().first()

        assertEquals(true, actualData)
    }

    @Test
    fun testGetUsername() = runTest {
        userDataStore.setUsername("ABC")
        val actualData = userDataStore.getUsername().first()

        assertEquals("ABC", actualData)
    }

    @Test
    fun testIsLogin() = runTest {
        userDataStore.setIsLogin(true)
        val actualData = userDataStore.getIsLogin().first()

        assertEquals(true, actualData)
    }

    @Test
    fun testSetUserDataSession() = runTest {
        val expectedUser = User(
            userName = "jacoco",
            userImage = "aaa",
            accessToken = "bbb",
            refreshToken = "ccc",
            expiresAt = 1234
        )

        userDataStore.setUserDataSession(expectedUser)
        val actualUser = userDataStore.getUserDataSession().first()

        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun testClearDataSession() = runTest {
        val user = User(
            userName = "jacoco",
            userImage = "aaa",
            accessToken = "bbb",
            refreshToken = "ccc",
            expiresAt = 1234
        )
        userDataStore.setUserDataSession(user)
        userDataStore.clearAllDataSession()

        val expectedUser = User(
            userName = null,
            userImage = null,
            accessToken = null,
            refreshToken = null,
            expiresAt = null
        )
        val actualUser = userDataStore.getUserDataSession().first()


        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun setUserToken() = runTest {
        val expectedToken = "999"
        userDataStore.setUserTokenSession(expectedToken)
        val actualToken = userDataStore.getUserDataSession().first()

        assertEquals(expectedToken,actualToken.accessToken)
    }

    @Test
    fun setUserAuth() = runTest {
        userDataStore.setUserAuthorization(true)
        val actualData = userDataStore.checkUserAuthorization().first()

        assertEquals(true , actualData)
    }


}