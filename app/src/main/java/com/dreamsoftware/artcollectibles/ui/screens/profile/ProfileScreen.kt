package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.elevatedShape
import androidx.compose.runtime.*
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
import com.dreamsoftware.artcollectibles.BuildConfig
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.CommonDatePicker
import com.dreamsoftware.artcollectibles.ui.components.CommonDefaultTextField
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

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
            navController = navController,
            state = state,
            onNameChanged = ::onNameChanged,
            onInfoChanged = ::onInfoChanged,
            onBirthdateChanged = ::onBirthdateChanged,
            onSaveClicked = ::saveUserInfo,
            onCloseSessionClicked = ::closeSession
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileComponent(
    context: Context,
    navController: NavController,
    state: ProfileUiState,
    onNameChanged: (String) -> Unit,
    onInfoChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
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
                        .data(state.userInfo?.photoUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
                state.accountBalance?.let {
                    CurrentAccountBalance(accountBalance = it) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(BuildConfig.MUMBAI_FAUCET_URL)
                            )
                        )
                    }
                }
                CommonDefaultTextField(
                    modifier = defaultModifier,
                    labelRes = R.string.profile_input_name_label,
                    placeHolderRes = R.string.profile_input_name_placeholder,
                    value = state.userInfo?.name,
                    onValueChanged = onNameChanged
                )
                CommonDefaultTextField(
                    modifier = defaultModifier,
                    isReadOnly = true,
                    labelRes = R.string.profile_input_contact_label,
                    placeHolderRes = R.string.profile_input_contact_placeholder,
                    value = state.userInfo?.contact
                )
                CommonDefaultTextField(
                    modifier = defaultModifier,
                    isReadOnly = true,
                    labelRes = R.string.profile_input_wallet_address_label,
                    placeHolderRes = R.string.profile_input_wallet_address_placeholder,
                    value = state.userInfo?.walletAddress
                )
                CommonDatePicker(
                    modifier = defaultModifier,
                    labelRes = R.string.profile_input_birthdate_label,
                    placeHolderRes = R.string.profile_input_birthdate_placeholder,
                    value = state.userInfo?.birthdate,
                    onValueChange = onBirthdateChanged
                )
                CommonDefaultTextField(
                    modifier = defaultModifier.height(150.dp),
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
}

@Composable
internal fun CurrentAccountBalance(accountBalance: AccountBalance, onGetMoreMaticClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.matic_icon),
            contentDescription = "Matic Icon",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )
        Text(
            text = stringResource(id = R.string.profile_current_matic, accountBalance.erc20.toString()),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        CommonButton(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = R.string.profile_get_more_matic,
            widthDp = 150.dp,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple700,
                contentColor = Color.White
            ),
            buttonShape = elevatedShape,
            onClick = onGetMoreMaticClicked
        )
    }
}