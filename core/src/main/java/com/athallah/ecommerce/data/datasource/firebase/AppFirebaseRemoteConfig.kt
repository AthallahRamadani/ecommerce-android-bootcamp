package com.athallah.ecommerce.data.datasource.firebase

import android.util.Log
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Payment
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AppFirebaseRemoteConfig(
    private val remoteConfig: FirebaseRemoteConfig
) {

    fun fetchPaymentMethod(): Flow<ResultState<List<Payment>>> = flow {
        emit(ResultState.Loading)
        try {
            remoteConfig.fetchAndActivate().await()

            val dataJson = remoteConfig.getString(REMOTE_DATA_KEY)
            Log.d("sss", "fetchPaymentMethod: ${dataJson}")

            val data = extractJsonToObject(dataJson)
            Log.d("sss", "fetchPaymentMethod: ${data}")



            emit(ResultState.Success(data))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    fun updateConfig(callback: (ResultState<List<Payment>>) -> Unit) {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("data")) {
                    remoteConfig.activate().addOnCompleteListener {
                        val dataJson = remoteConfig.getString(REMOTE_DATA_KEY)
                        val data = extractJsonToObject(dataJson)
                        callback(ResultState.Success(data))
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                callback(ResultState.Error(error))
            }

        })
    }

    private fun extractJsonToObject(dataJson: String): ArrayList<Payment> {
        return Gson().fromJson(
            dataJson,
            object : TypeToken<ArrayList<Payment>>() {}.type
        ) ?: ArrayList()
    }

    companion object {
        private const val REMOTE_DATA_KEY = "data"
    }


}