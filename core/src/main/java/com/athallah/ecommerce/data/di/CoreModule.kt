package com.athallah.ecommerce.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.datasource.room.AppDatabase
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.AppRepositoryImpl
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.CartRepositoryImpl
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.data.repo.FulfillmentRepositoryImpl
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.StoreRepositoryImpl
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.data.repo.UserRepositoryImpl
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.data.repo.WishlistRepositoryImpl
import com.athallah.ecommerce.utils.Constant
import com.athallah.ecommerce.utils.extension.HeaderInterceptor
import com.athallah.ecommerce.utils.extension.SupportAuthenticator
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "APPLICATION_PREFERENCE")

val repositoryModule = module {
    single { AppRepositoryImpl(get(), get(), get()) } bind AppRepository::class
    single { UserRepositoryImpl(get(), get()) } bind UserRepository::class
    single { StoreRepositoryImpl(get(), get()) } bind StoreRepository::class
    single { WishlistRepositoryImpl(get()) } bind WishlistRepository::class
    single { CartRepositoryImpl(get()) } bind CartRepository::class
    single { FulfillmentRepositoryImpl(get()) } bind FulfillmentRepository::class
}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "database.db"
        ).build()
    }
    single {
        get<AppDatabase>().wishlistDao()
    }
    single {
        get<AppDatabase>().cartDao()
    }
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
            .client(get())
            .build()
    }

    single { HeaderInterceptor(get()) }
    single { SupportAuthenticator(androidContext(), get()) }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(get<ChuckerInterceptor>())
            addInterceptor(get<HeaderInterceptor>())
            authenticator(get<SupportAuthenticator>())
        }.build()
    }

    single {
        ChuckerInterceptor.Builder(androidApplication())
            .collector(ChuckerCollector(androidApplication()))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}





