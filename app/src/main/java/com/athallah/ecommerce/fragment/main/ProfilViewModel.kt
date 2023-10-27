package com.athallah.ecommerce.fragment.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilViewModel @Inject constructor(
    private val appRepository: com.athallah.ecommerce.data.AppRepository
) : ViewModel() {

    var currentImageUri: Uri? = null
}
