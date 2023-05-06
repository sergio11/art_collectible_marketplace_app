package com.dreamsoftware.artcollectibles.ui.screens.edit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.theme.Purple700


data class EditNftScreenArgs(
    val metadataCid: String
)

@Composable
fun EditNftScreen(
    args: EditNftScreenArgs,
    viewModel: EditNftViewModel = hiltViewModel(),
    onExitClicked: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val state by produceUiState(
        initialState = EditNftUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load(metadataCid = args.metadataCid)
        }
        EditNftComponent(
            state = state,
            snackBarHostState = snackBarHostState,
            onNameChanged = ::onNameChanged,
            onDescriptionChanged = ::onDescriptionChanged,
            onAddNewTag = ::onAddNewTag,
            onDeleteTag = ::onDeleteTag,
            onBackPressed = onExitClicked,
            onSaveClicked = ::save
        )
    }
}

@Composable
internal fun EditNftComponent(
    state: EditNftUiState,
    snackBarHostState: SnackbarHostState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    onBackPressed: () -> Unit,
    onSaveClicked: () -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            snackBarHostState = snackBarHostState,
            titleRes = R.string.edit_nft_main_title_text,
            centerTitle = true,
            enableVerticalScroll = true,
            navigationAction = TopBarAction(
                iconRes = R.drawable.back_icon,
                onActionClicked = onBackPressed
            ),
            screenContent = {
                CommonCardColumn {
                    val defaultModifier = Modifier
                        .padding(vertical = 15.dp)
                        .width(300.dp)
                    CommonAsyncImage(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        context = LocalContext.current,
                        imageUrl = tokenImageUrl
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    CommonDefaultTextField(
                        modifier = defaultModifier,
                        labelRes = R.string.add_nft_input_name_label,
                        placeHolderRes = R.string.add_nft_input_name_placeholder,
                        value = tokenName,
                        onValueChanged = onNameChanged
                    )
                    CommonTagsInputComponent(
                        modifier = defaultModifier,
                        titleRes = R.string.add_nft_input_related_topic_label,
                        placeholderRes = R.string.add_nft_input_related_topic_placeholder,
                        tagList = tokenTags,
                        onAddNewTag = onAddNewTag,
                        onDeleteTag = onDeleteTag
                    )
                    CommonDefaultTextField(
                        modifier = defaultModifier.height(150.dp),
                        labelRes = R.string.add_nft_input_description_label,
                        placeHolderRes = R.string.add_nft_input_description_placeholder,
                        value = tokenDescription,
                        isSingleLine = false,
                        onValueChanged = onDescriptionChanged
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    CommonButton(
                        enabled = !isLoading,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.edit_nft_save_button_text,
                        containerColor = Purple700,
                        contentColor = Color.White,
                        onClick = onSaveClicked
                    )
                    CommonButton(
                        enabled = !isLoading,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.edit_nft_cancel_button_text,
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        onClick = onBackPressed
                    )
                }
            }
        )
    }
}