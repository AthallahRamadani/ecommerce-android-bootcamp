package com.athallah.ecommerce.data.repo

import android.accounts.AuthenticatorException
import android.content.res.Resources.NotFoundException
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.response.ErrorResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.utils.extension.toUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): Flow<ResultState<User>>
}

class UserRepositoryImpl(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun register(email: String, password: String): Flow<ResultState<User>> = flow {
        emit(ResultState.Loading)
        try {
            val request = AuthRequest(email, password)
            val response = apiService.register(request)
            val user = response.data?.toUser()
            if (user != null) {
                emit(ResultState.Success(user))
            } else
                throw Exception()
        } catch (e: Exception) {
            val message = getApiErrorMessage(e)
            emit(ResultState.Error(message.toString()))
        }
    }

    override suspend fun login(email: String, password: String): User {
        val request = AuthRequest(
            email = email,
            password = password
        )
        val response = apiService.login(request)
        when (response.code) {
            HttpURLConnection.HTTP_OK -> {
                val data = response.data
                if (data != null) {
                    return data.toUser()
                }
                throw NotFoundException("No data from server")
            }
            HttpURLConnection.HTTP_BAD_REQUEST -> throw AuthenticatorException(response.message)
            HttpsURLConnection.HTTP_FORBIDDEN -> throw AuthenticatorException(response.message)
            else -> throw UnknownError(response.message)
        }
    }

    private fun getApiErrorMessage(e: Exception): String? {
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