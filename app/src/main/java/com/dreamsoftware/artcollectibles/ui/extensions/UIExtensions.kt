package com.dreamsoftware.artcollectibles.ui.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dreamsoftware.artcollectibles.ui.model.TabUi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.DecimalFormat
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

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label", text)
    clipboard?.setPrimaryClip(clip)
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

fun Date.format(withTime: Boolean = true): String =
    SimpleDateFormat(
        if (withTime) {
            "yyyy-MM-dd HH:mm:ss"
        } else {
            "yyyy-MM-dd"
        }, Locale.getDefault()
    ).format(this)


fun Double.format(): String =
    DecimalFormat("0.00").format(this)


fun <T>Iterable<TabUi<T>>.tabSelectedIndex() = indexOfFirst { it.isSelected }

fun <T>Iterable<TabUi<T>>.tabSelectedTitle() = find { it.isSelected }?.titleRes

fun <T>Iterable<TabUi<T>>.tabSelectedTypeOrDefault(default: T) = find { it.isSelected }?.type ?: default


