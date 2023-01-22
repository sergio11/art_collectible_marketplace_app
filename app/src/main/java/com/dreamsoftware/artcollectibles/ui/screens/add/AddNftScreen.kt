package com.dreamsoftware.artcollectibles.ui.screens.add

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.ui.components.CameraDisplayComponent
import com.dreamsoftware.artcollectibles.ui.extensions.checkPermissionState

@Composable
fun AddNftScreen(
    navController: NavController,
    viewModel: AddNftViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val isCameraPermissionGranted = rememberSaveable { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isSuccess ->
        if (isSuccess) {
            isCameraPermissionGranted.value = true
        } else {
        }
    }
    // run on every composition
    SideEffect {
        context.checkPermissionState(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = {
                isCameraPermissionGranted.value = true
            },
            onPermissionDenied = {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        )
    }
    AddNftComponent(
        lifecycleOwner = lifecycleOwner,
        context = context,
        isCameraPermissionGranted = isCameraPermissionGranted.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNftComponent(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    isCameraPermissionGranted: Boolean
) {
    if(isCameraPermissionGranted) {
        CameraDisplayComponent(
            lifecycleOwner = lifecycleOwner,
            context = context,
            onImageCaptured = {},
            onError = {}
        )
    }
}