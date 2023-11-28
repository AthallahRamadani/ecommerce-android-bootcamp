package com.athallah.ecommerce.data.repo

import android.util.Log
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.response.ErrorResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.firebase.FirebaseSubscribe
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.toBearerToken
import com.athallah.ecommerce.utils.extension.toMultipartBody
import com.athallah.ecommerce.utils.extension.toUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDataStore: UserDataStore,
    private val firebaseSubscribe: FirebaseSubscribe
) : UserRepository {

    override suspend fun register(email: String, password: String): User {
        firebaseSubscribe.subscribe()
        val firebaseToken = firebaseSubscribe.firebaseToken()
        Log.d("sss", "register: $firebaseToken")
        val request = AuthRequest(
            email = email,
            password = password,
            firebaseToken = firebaseToken
        )
        val response = apiService.register(request)
        return response.data?.toUser() ?: throw Exception("Error uWu")
    }

    override suspend fun login(email: String, password: String): User {
        firebaseSubscribe.subscribe()
        val firebaseToken = firebaseSubscribe.firebaseToken()
        Log.d("sss", "login: $firebaseToken")
        val request = AuthRequest(
            email = email,
            password = password,
            firebaseToken = firebaseToken
        )
        val response = apiService.login(request)
        return response.data?.toUser() ?: throw Exception("Error uWu")
    }

    override fun uploadProfile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Flow<ResultState<Boolean>> =
        flow {
            emit(ResultState.Loading)
            try {
                val response: ProfileResponse<ProfileDataResponse> =
                    apiService.profile(userName, userImage)

                val dataResponse = response.data
                val user = dataResponse?.toUser()
                if (user != null) {
                    userDataStore.setUserDataSession(user)
                    emit(ResultState.Success(true))
                } else {
                    throw Exception("konz")
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }
}