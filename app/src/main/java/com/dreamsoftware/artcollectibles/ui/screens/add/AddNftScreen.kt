package com.dreamsoftware.artcollectibles.ui.screens.add

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.extensions.checkPermissionState
import com.dreamsoftware.artcollectibles.ui.extensions.getCacheSubDir
import com.dreamsoftware.artcollectibles.ui.extensions.getMimeType
import com.dreamsoftware.artcollectibles.ui.extensions.getUriForFile
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun AddNftScreen(
    navController: NavController,
    viewModel: AddNftViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = AddNftUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val outputDirectory = context.getCacheSubDir("nft_images")
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val isCameraPermissionGranted = rememberSaveable { mutableStateOf(false) }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isSuccess ->
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
    with(viewModel) {
        AddNftComponent(
            state = state,
            lifecycleOwner = lifecycleOwner,
            context = context,
            isCameraPermissionGranted = isCameraPermissionGranted.value,
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = {
                with(context) {
                    onImageSelected(
                        imageUri = getUriForFile(getFileProviderAuthority(), it),
                        mimeType = getMimeType(Uri.fromFile(it)).orEmpty()
                    )
                }
            },
            onNameChanged = ::onNameChanged,
            onDescriptionChanged = ::onDescriptionChanged,
            onCreateClicked = {},
            onCancelClicked = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
internal fun AddNftComponent(
    state: AddNftUiState,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    outputDirectory: File,
    executor: Executor,
    isCameraPermissionGranted: Boolean,
    onImageCaptured: (File) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCreateClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    if (isCameraPermissionGranted) {
        if (state.imageUri == null) {
            CameraDisplayComponent(
                lifecycleOwner = lifecycleOwner,
                context = context,
                outputDirectory = outputDirectory,
                executor = executor,
                onImageCaptured = onImageCaptured,
                onError = {
                    Log.d("ART_COLLE", "ImageCaptureException -> ${it.message} CALLED!")
                }
            )
        } else {
            AddNftForm(
                state = state,
                onNameChanged = onNameChanged,
                onDescriptionChanged = onDescriptionChanged,
                onCreateClicked = onCreateClicked,
                onCancelClicked = onCancelClicked
            )
        }
    } else {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNftForm(
    state: AddNftUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCreateClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(imageRes = R.drawable.common_background)
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = stringResource(R.string.add_nft_main_title_text),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.headlineLarge
                )
                Card(
                    modifier = Modifier.padding(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(27.dp),
                    border = BorderStroke(3.dp, Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val defaultModifier = Modifier
                            .padding(vertical = 20.dp)
                            .width(300.dp)
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.imageUri)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.user_placeholder),
                            contentDescription = stringResource(R.string.image_content_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(250.dp).clip(CircleShape)
                        )
                        CommonDefaultTextField(
                            modifier = defaultModifier,
                            labelRes = R.string.add_nft_input_name_label,
                            placeHolderRes = R.string.add_nft_input_name_placeholder,
                            value = state.name,
                            onValueChanged = onNameChanged
                        )
                        CommonDefaultTextField(
                            modifier = defaultModifier.height(150.dp),
                            labelRes = R.string.add_nft_input_description_label,
                            placeHolderRes = R.string.add_nft_input_description_placeholder,
                            value = state.description,
                            isSingleLine = false,
                            onValueChanged = onDescriptionChanged
                        )
                        CommonButton(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .width(300.dp),
                            text = R.string.add_nft_create_button_text,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple700,
                                contentColor = Color.White
                            ),
                            onClick = onCreateClicked
                        )
                        CommonButton(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .width(300.dp),
                            text = R.string.add_nft_cancel_button_text,
                            onClick = onCancelClicked
                        )
                    }
                }
            }
        }
    }
}