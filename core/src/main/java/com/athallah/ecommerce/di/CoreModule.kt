package com.athallah.ecommerce.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.athallah.ecommerce.data.AppRepository
import com.athallah.ecommerce.data.ImpAppRepository
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "APPLICATION_PREFERENCE")

val preferenceModule = module {
    single { androidContext().dataStore }
    single { UserDataStore(get()) }
}

val repositoryModule = module {
    factory<ImpAppRepository> { AppRepository(get()) }
}




