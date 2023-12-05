package com.athallah.ecommerce.utils.extension

import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.Constant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val preferences: UserDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.toString()
        val isPreloginRequest = Constant.PRELOGIN_ENDPOINT.contains(requestUrl)

        val newRequest = originalRequest.newBuilder().apply {
            if (isPreloginRequest) {
                addHeader("API_KEY", Constant.API_KEY)
            } else {
                val user = runBlocking { preferences.getUserDataSession().first() }
                val bearerToken: String = user.accessToken?.toBearerToken() ?: ""
                addHeader("Authorization", bearerToken)
            }
        }.build()
        return chain.proceed(newRequest)
    }
}
