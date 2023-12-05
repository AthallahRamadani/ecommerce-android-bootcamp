package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.firebase.FirebaseSubscribe
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDataStore: UserDataStore,
    private val firebaseSubscribe: FirebaseSubscribe,
    private val appPreferences: UserDataStore
) : UserRepository {

    override suspend fun register(email: String, password: String): Flow<ResultState<Boolean>> =
        flow {
            emit(ResultState.Loading)
            try {
                val firebaseToken = firebaseSubscribe.firebaseToken()
                val request = AuthRequest(
                    email,
                    password,
                    firebaseToken
                )
                val response = apiService.register(request)
                val user = response.data?.toUser()
                if (user != null) {
                    appPreferences.setUserDataSession(user)
                    appPreferences.setUserAuthorization(true)
                    firebaseSubscribe.subscribe()
                    emit(ResultState.Success(true))
                } else {
                    throw Exception("Failed")
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }

    override suspend fun login(email: String, password: String): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        try {
            val firebaseToken = firebaseSubscribe.firebaseToken()
            val request = AuthRequest(
                email,
                password,
                firebaseToken
            )
            val response = apiService.login(request)
            val user = response.data?.toUser()
            if (user != null) {
                appPreferences.setUserDataSession(user)
                appPreferences.setUserAuthorization(true)
                firebaseSubscribe.subscribe()
                emit(ResultState.Success(true))
            } else {
                throw Exception("Failed")
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
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
