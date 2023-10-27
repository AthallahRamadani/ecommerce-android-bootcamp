package com.athallah.ecommerce.di

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.athallah.ecommerce.data.AppRepository
import com.athallah.ecommerce.data.source.local.preference.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.prefs.Preferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun providePreference(context: Context): com.athallah.ecommerce.data.source.local.preference.SharedPref {
        return com.athallah.ecommerce.data.source.local.preference.SharedPref.getInstance(context)
    }
    @Singleton
    @Provides
    fun provideRepositories(sharedPref: com.athallah.ecommerce.data.source.local.preference.SharedPref): com.athallah.ecommerce.data.AppRepository {
        return com.athallah.ecommerce.data.AppRepository(sharedPref)
    }
}