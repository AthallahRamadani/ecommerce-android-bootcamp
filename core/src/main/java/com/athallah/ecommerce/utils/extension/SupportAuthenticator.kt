package com.athallah.ecommerce.utils.extension

import android.content.Context
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.datasource.api.request.RefreshRequest
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.Constant
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SupportAuthenticator(
    private val context: Context,
    private val preferance: UserDataStore
) : okhttp3.Authenticator {

    private val apiService: ApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .apply {
                addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .collector(
                            ChuckerCollector(
                                context = context,
                                showNotification = true,
                                retentionPeriod = RetentionManager.Period.ONE_HOUR
                            )
                        )
                        .build()
                )
                addInterceptor(HeaderInterceptor(preferance))
            }
            .build()

        val retrofit = Retrofit.Builder().baseUrl(Constant.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService
    }


    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            try {
                val newToken = getNewToken()

                return response.request.newBuilder()
                    .header("Authorization", newToken.toBearerToken())
                    .build()
            } catch (e: Exception) {
                runBlocking { preferance.clearAllDataSession() }
            }
            return null
        }
    }

    private fun getNewToken(): String {
        val user =
            runBlocking { preferance.getUserDataSession().first() }
        val refreshToken = user.refreshToken ?: ""

        val newToken = runBlocking {
            val refreshResponse = apiService.refresh(RefreshRequest(refreshToken))
            refreshResponse.token?.let { preferance.setUserTokenSession(it) }

            return@runBlocking preferance.getUserDataSession().first<User>().accessToken
        }

        return newToken ?: ""
    }
}