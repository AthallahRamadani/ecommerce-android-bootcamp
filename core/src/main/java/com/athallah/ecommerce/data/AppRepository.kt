package com.athallah.ecommerce.data

import com.athallah.ecommerce.data.source.local.preference.SharedPref
import javax.inject.Inject
import javax.sql.CommonDataSource

class AppRepository @Inject constructor(
    private val sharedPref: SharedPref
)  {
     fun setIsFirstLaunchToFalse() {
        sharedPref.setIsFirstLaunchToFalse()
    }

    fun isFirstLaunch() : Boolean {
        return sharedPref.isFirstLaunch()
    }

}