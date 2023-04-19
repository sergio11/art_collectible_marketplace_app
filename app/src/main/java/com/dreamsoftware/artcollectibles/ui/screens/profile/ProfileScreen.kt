package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.Manifest
import android.content.Context
import android.net.Uri
import android.webkit.*
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.elevatedShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTopAppBar
import com.dreamsoftware.artcollectibles.ui.components.core.TopBarAction
import com.dreamsoftware.artcollectibles.ui.components.countrypicker.CountryPickerField
import com.dreamsoftware.artcollectibles.ui.extensions.checkPermissionState
import com.dreamsoftware.artcollectibles.ui.extensions.createTempImageFile
import com.dreamsoftware.artcollectibles.ui.theme.*

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSessionClosed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = ProfileUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.isSessionClosed) {
                    onSessionClosed()
                } else {
                    value = it
                }
            }
        }
    }
    with(viewModel) {
        LaunchedEffect(isProfileLoaded()) {
            if (!isProfileLoaded()) {
                load()
            }
        }
        ProfileComponent(
            context = LocalContext.current,
            fileProviderId = getFileProviderAuthority(),
            navController = navController,
            state = state,
            onNameChanged = ::onNameChanged,
            onLocationChanged = ::onLocationChanged,
            onProfessionalTitleChanged = ::onProfessionalTitleChanged,
            onInfoChanged = ::onInfoChanged,
            onPictureChanged = ::onPictureChanged,
            onBirthdateChanged = ::onBirthdateChanged,
            onCountryChanged = ::onCountryChanged,
            onSaveClicked = ::saveUserInfo,
            onInstagramNickChanged = ::onInstagramNickChanged,
            onCloseSessionClicked = ::closeSession,
            onAddNewTag = ::onAddNewTag,
            onDeleteTag = ::onDeleteTag
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileComponent(
    context: Context,
    fileProviderId: String,
    navController: NavController,
    state: ProfileUiState,
    onNameChanged: (String) -> Unit,
    onProfessionalTitleChanged: (String) -> Unit,
    onLocationChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onInfoChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onPictureChanged: (Uri) -> Unit,
    onInstagramNickChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onCloseSessionClicked: () -> Unit,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit
) {
    var isProfilePicturePicker by rememberSaveable { mutableStateOf(false) }
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        topBar = {
            CommonTopAppBar(titleRes = R.string.profile_main_title_text, menuActions = listOf(
                TopBarAction(
                    iconRes = R.drawable.settings_icon,
                    onActionClicked = {

                    }
                )
            ))
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(imageRes = R.drawable.screen_background_2)
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Card(
                    modifier = Modifier.padding(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.7f)),
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
                        UserAccountProfilePicture(size = 150.dp, userInfo = state.userInfo) {
                            if (state.userInfo?.externalProviderAuthType == null) {
                                isProfilePicturePicker = true
                            }
                        }
                        state.accountBalance?.let {
                            CurrentAccountBalance(
                                modifier = Modifier.padding(vertical = 8.dp),
                                iconSize = 30.dp,
                                textSize = 20.sp,
                                textColor = DarkPurple,
                                accountBalance = it
                            )
                        }
                        CommonDefaultTextField(
                            labelRes = R.string.profile_input_name_label,
                            placeHolderRes = R.string.profile_input_name_placeholder,
                            value = state.userInfo?.name,
                            onValueChanged = onNameChanged
                        )
                        CommonDefaultTextField(
                            labelRes = R.string.profile_input_professional_title_label,
                            placeHolderRes = R.string.profile_input_professional_title_placeholder,
                            value = state.userInfo?.professionalTitle,
                            onValueChanged = onProfessionalTitleChanged
                        )
                        CountryPickerField(
                            labelRes = R.string.profile_input_country_label,
                            placeHolderRes = R.string.profile_input_country_placeholder,
                            selectedCountry = state.userInfo?.country,
                            pickedCountry = onCountryChanged
                        )
                        CommonDefaultTextField(
                            labelRes = R.string.profile_input_location_label,
                            placeHolderRes = R.string.profile_input_location_placeholder,
                            value = state.userInfo?.location,
                            onValueChanged = onLocationChanged
                        )
                        CommonDefaultTextField(
                            isReadOnly = true,
                            labelRes = R.string.profile_input_contact_label,
                            placeHolderRes = R.string.profile_input_contact_placeholder,
                            value = state.userInfo?.contact
                        )
                        CommonDefaultTextField(
                            isReadOnly = true,
                            labelRes = R.string.profile_input_wallet_address_label,
                            placeHolderRes = R.string.profile_input_wallet_address_placeholder,
                            value = state.userInfo?.walletAddress
                        )
                        CommonDatePicker(
                            labelRes = R.string.profile_input_birthdate_label,
                            placeHolderRes = R.string.profile_input_birthdate_placeholder,
                            value = state.userInfo?.birthdate,
                            onValueChange = onBirthdateChanged
                        )
                        TagsInputComponent(
                            tagList = state.userInfo?.tags.orEmpty(),
                            titleRes = R.string.profile_input_tags_label,
                            placeholderRes = R.string.profile_input_tags_placeholder,
                            onAddNewTag = onAddNewTag,
                            onDeleteTag = onDeleteTag
                        )
                        SocialNetworkField(
                            labelRes = R.string.profile_input_instagram_label,
                            placeHolderRes = R.string.profile_input_instagram_placeholder,
                            value = state.userInfo?.instagramNick,
                            onValueChanged = onInstagramNickChanged
                        )
                        CommonDefaultTextField(
                            modifier = CommonDefaultTextFieldModifier.height(200.dp),
                            labelRes = R.string.profile_input_info_label,
                            placeHolderRes = R.string.profile_input_info_placeholder,
                            value = state.userInfo?.info,
                            isSingleLine = false,
                            onValueChanged = onInfoChanged
                        )
                        CommonButton(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .width(300.dp),
                            text = R.string.profile_save_button_text,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple700,
                                contentColor = Color.White
                            ),
                            onClick = onSaveClicked
                        )
                        CommonButton(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .width(300.dp),
                            text = R.string.profile_sign_off_button_text,
                            onClick = onCloseSessionClicked
                        )
                    }
                }
            }
            ProfilePicturePicker(
                context = context,
                fileProviderId = fileProviderId,
                modifier = Modifier
                    .zIndex(2f)
                    .height(400.dp)
                    .align(Alignment.BottomCenter),
                isVisible = isProfilePicturePicker,
                onImageSelected = onPictureChanged
            ) { isProfilePicturePicker = false }
        }
    }
}

@Composable
internal fun ProfilePicturePicker(
    context: Context,
    modifier: Modifier,
    fileProviderId: String,
    isVisible: Boolean,
    onImageSelected: (Uri) -> Unit,
    onPickerClosed: () -> Unit
) {
    if (isVisible) {
        val photoTmpFile by remember {
            mutableStateOf(context.createTempImageFile(fileProviderId))
        }
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let(onImageSelected)
                onPickerClosed()
            }
        val takePhotoErrorOccurred = stringResource(id = R.string.profile_take_photo_error_occurred)
        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    onImageSelected(photoTmpFile)
                    onPickerClosed()
                } else {
                    Toast.makeText(context, takePhotoErrorOccurred, Toast.LENGTH_SHORT).show()
                }
            }
        val permissionGrantedText = stringResource(id = R.string.profile_camera_permission_granted)
        val permissionDeniedText = stringResource(id = R.string.profile_camera_permission_denied)
        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(context, permissionGrantedText, Toast.LENGTH_SHORT).show()
                    cameraLauncher.launch(photoTmpFile)
                } else {
                    Toast.makeText(context, permissionDeniedText, Toast.LENGTH_SHORT).show()
                }
            }
        BottomSheetLayout(
            modifier,
            onBottomSheetClosed = onPickerClosed
        ) {
            Text(
                text = stringResource(id = R.string.profile_picture_picker_title),
                textAlign = TextAlign.Center,
                color = Purple500,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.headlineSmall
            )
            CommonButton(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                text = R.string.profile_pick_image_from_gallery,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple200,
                    contentColor = Color.White
                ),
                buttonShape = elevatedShape,
                onClick = { galleryLauncher.launch("image/*") }
            )
            CommonButton(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                text = R.string.profile_pick_from_camera,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                buttonShape = elevatedShape,
                onClick = {
                    context.checkPermissionState(
                        permission = Manifest.permission.CAMERA,
                        onPermissionGranted = {
                            cameraLauncher.launch(photoTmpFile)
                        },
                        onPermissionDenied = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }
            )
        }
    }
}