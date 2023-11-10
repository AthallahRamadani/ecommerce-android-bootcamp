package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.response.ErrorResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.toBearerToken
import com.athallah.ecommerce.utils.extension.toMultipartBody
import com.athallah.ecommerce.utils.extension.toUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.File

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDataStore: UserDataStore
) : UserRepository {

    override suspend fun register(email: String, password: String): User {
        val request = AuthRequest(
            email = email,
            password = password
        )
        val response = apiService.register(request)
        return response.data?.toUser() ?: throw Exception("Error uWu")
    }

    override suspend fun login(email: String, password: String): User {
        val request = AuthRequest(
            email = email,
            password = password
        )
        val response = apiService.login(request)
        return response.data?.toUser() ?: throw Exception("Error uWu")
    }

    override fun uploadProfile(userName: String, userImage: File?): Flow<ResultState<Boolean>> =
        flow {
            emit(ResultState.Loading)
            try {
                val userNamePart = MultipartBody.Part.createFormData("userName", userName)
//                val userNameRequestBody = userName.toRequestBody("text/plain".toMediaType())
                val userImagePart = userImage?.toMultipartBody("userImage")
                val token = userDataStore.getAccToken().first().toBearerToken()
                val response: ProfileResponse<ProfileDataResponse> = apiService.profile(userNamePart, userImagePart, token)

                val dataResponse = response.data
                val user = dataResponse?.toUser()
                if (user != null) {
                    userDataStore.setUserDataSession(user)
                    emit(ResultState.Success(true))
                } else {
                    emit(ResultState.Success(false))
                }
            } catch (e: Exception) {
                val message = getApiErrorMessage(e)
                emit(ResultState.Error(message.toString()))
            }
        }




    fun getApiErrorMessage(e: Throwable): String? {
        var message = e.message
        if (e is HttpException) {
            val errorResponse =
                Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    ErrorResponse::class.java
                ) ?: ErrorResponse()
            errorResponse.message?.let { message = it }
        }
        return message
    }


}