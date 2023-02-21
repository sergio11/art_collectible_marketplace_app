package com.dreamsoftware.artcollectibles.ui.extensions

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume


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
    return getUriForFile(
        providerId, File.createTempFile(
            imageFileName,
            ".jpg",
            externalCacheDir
        )
    )
}

fun Context.getUriForFile(providerId: String, file: File): Uri =
    FileProvider.getUriForFile(this, providerId, file)

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

fun Context.checkPermissionState(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(this, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    } else {
        onPermissionDenied()
    }
}

fun Context.getCacheSubDir(name: String) = File(cacheDir, name).also {
    if (!it.exists()) {
        it.mkdir()
    }
}

fun Context.getMimeType(uri: Uri): String? =
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(contentResolver.getType(uri))
    } else {
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(uri.path?.let { File(it) }).toString())
    }

fun Date.format(): String =
    SimpleDateFormat("yyyy-mm-dd").format(this)


