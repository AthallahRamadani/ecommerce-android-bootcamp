package com.athallah.ecommerce.fragment.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

object PhotoLoaderManager {


    private const val PHOTOS_DIR = "photos"
    private const val FILE_PROVIDER = "fileprovider"


    fun isCameraPermissionsGranted(context: Context) =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    fun buildNewUri(context: Context): Uri {
        val photosDir = File(context.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${context.packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(context, authority, photoFile)
    }

    private fun generateFilename() = "photos-${System.currentTimeMillis()}.jpg"

}