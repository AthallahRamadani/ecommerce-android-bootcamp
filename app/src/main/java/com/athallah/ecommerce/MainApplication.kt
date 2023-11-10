package com.athallah.ecommerce

import android.app.Application
import com.athallah.ecommerce.data.di.apiModule
import com.athallah.ecommerce.data.di.preferenceModule
import com.athallah.ecommerce.data.di.repositoryModule
import com.athallah.ecommerce.data.di.roomModule
import com.athallah.ecommerce.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(Level.NONE)
            androidContext(this@MainApplication)
            modules(
                listOf(
                    //core
                    preferenceModule,
                    repositoryModule,
                    apiModule,
                    roomModule,
                    //app
                    vmModule
                )
            )
        }
    }
}