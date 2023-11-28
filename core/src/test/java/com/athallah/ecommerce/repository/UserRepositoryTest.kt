package com.athallah.ecommerce.repository

import app.cash.turbine.test
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.request.ProfileRequest
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.firebase.FirebaseSubscribe
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.data.repo.UserRepositoryImpl
import com.athallah.ecommerce.utils.extension.toUser
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val apiService: ApiService = mock()
    private val userDataStore: UserDataStore = mock()
    private val firebaseSubscribe: FirebaseSubscribe = mock()

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(
            apiService, userDataStore, firebaseSubscribe
        )
    }

    @Test
    fun register() = runTest {

        val request = AuthRequest(
            email = "udin",
            password = "password",
            firebaseToken = "firebaseToken"
        )
        val registerResponse = RegisterResponse(
            code = 200,
            message = "d",
            data = RegisterDataResponse(
                "2",
                2,
                "2",
            )
        )
        whenever(firebaseSubscribe.firebaseToken()).thenReturn("firebaseToken")
        whenever(apiService.register(request)).thenReturn(registerResponse)
        val actualData = userRepository.register("udin", "password")
        verify(firebaseSubscribe).subscribe()


        assertEquals(registerResponse.data!!.toUser(), actualData)
    }

    @Test
    fun login() = runTest {
        val request = AuthRequest(
            email = "udin",
            password = "password",
            firebaseToken = "firebaseToken"
        )
        val loginResponse = LoginResponse(
            code = 200,
            message = "d",
            data = LoginDataResponse(
                "2",
                "2",
                "2",
                3600,
                ""
            )
        )

        whenever(firebaseSubscribe.firebaseToken()).thenReturn("firebaseToken")
        whenever(apiService.login(request)).thenReturn(loginResponse)
        val actualData = userRepository.login("udin", "password")
        verify(firebaseSubscribe).subscribe()


        assertEquals(loginResponse.data!!.toUser(), actualData)

    }

    @Test
    fun uploadProfile() = runTest {
        val profileRequestBody = ProfileRequest(
            "budiman",
            ""
        )
        val profileResponse = ProfileResponse(
            200,
            "",
            ProfileDataResponse(
                "",
                "budiman"
            )
        )
        val nameRequestBody = profileRequestBody.userName.toRequestBody()
        whenever(userDataStore.getAccToken()).thenReturn(flowOf("sss"))
        whenever(apiService.profile(userName = nameRequestBody, userImage = null)).thenReturn(
            profileResponse
        )
        userRepository.uploadProfile(nameRequestBody, null).test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(ResultState.Success(true), awaitItem())
            awaitComplete()
        }
        verify(userDataStore).setUserDataSession(profileResponse.data!!.toUser())
    }
}