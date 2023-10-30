package com.athallah.ecommerce.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.AppRepositoryImpl
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.data.repo.UserRepositoryImpl
import com.athallah.ecommerce.utils.Constant
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "APPLICATION_PREFERENCE")

val repositoryModule = module {
    factory<AppRepository> { AppRepositoryImpl(get()) }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}

val preferenceModule = module {
    single { androidContext().dataStore }
    single { UserDataStore(get()) }
}

val apiModule = module {
    single<Retrofit> {
        return@single Retrofit.Builder()
            .baseUrl(Constant.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ApiService> {
        get<Retrofit>(Retrofit::class).create(ApiService::class.java)
    }
}





