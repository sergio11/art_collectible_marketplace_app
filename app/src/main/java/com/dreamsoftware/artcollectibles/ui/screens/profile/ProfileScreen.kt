package com.dreamsoftware.artcollectibles.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.CommonDefaultTextField
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = ProfileUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    LaunchedEffect(viewModel.isProfileLoaded()) {
        if (!viewModel.isProfileLoaded()) {
            viewModel.loadProfile()
        }
    }
    ProfileComponent(
        navController = navController,
        state = state,
        onSaveClicked = {

        },
        onCloseSessionClicked = {
            viewModel.closeSession()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileComponent(
    navController: NavController,
    state: ProfileUiState,
    onSaveClicked: () -> Unit,
    onCloseSessionClicked: () -> Unit
) {
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile_main_title_text),
                textAlign = TextAlign.Center,
                color = Purple500,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .width(300.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.userInfo?.photoUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).clip(CircleShape)
                )
                CommonDefaultTextField(
                    modifier = Modifier.padding(vertical = 20.dp),
                    labelRes = R.string.profile_input_name_label,
                    placeHolderRes = R.string.profile_input_name_placeholder,
                    value = state.userInfo?.name,
                    onValueChanged = {

                    }
                )
                CommonDefaultTextField(
                    modifier = Modifier.padding(vertical = 20.dp),
                    isEnabled = false,
                    labelRes = R.string.profile_input_wallet_address_label,
                    placeHolderRes = R.string.profile_input_wallet_address_placeholder,
                    value = state.userInfo?.walletAddress,
                    onValueChanged = {

                    }
                )
                CommonDefaultTextField(
                    modifier = Modifier.padding(vertical = 20.dp)
                        .height(150.dp),
                    labelRes = R.string.profile_input_info_label,
                    placeHolderRes = R.string.profile_input_info_placeholder,
                    value = state.userInfo?.info,
                    isSingleLine = false,
                    onValueChanged = {

                    }
                )
                CommonButton(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = R.string.profile_save_button_text,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple700,
                        contentColor = Color.White
                    ),
                    onClick = onSaveClicked
                )
                CommonButton(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = R.string.profile_sign_off_button_text,
                    onClick = onCloseSessionClicked
                )
            }
        }
    }
}