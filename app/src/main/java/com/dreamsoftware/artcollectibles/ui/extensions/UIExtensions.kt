package com.dreamsoftware.artcollectibles.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity cannot be found")
}

fun Context.createTempImageFile(providerId: String): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return getUriForFile(providerId, File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    ))
}

fun Context.getUriForFile(providerId: String, file: File) =
    FileProvider.getUriForFile(this,providerId , file)


