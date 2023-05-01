package com.dreamsoftware.artcollectibles.ui.screens.commentdetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectiblesRow
import com.dreamsoftware.artcollectibles.ui.components.UserFollowersInfoComponent
import com.dreamsoftware.artcollectibles.ui.components.UserStatisticsComponent
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import java.math.BigInteger

data class CommentDetailScreenArgs(
    val uid: String
)

@Composable
fun CommentDetailScreen(
    args: CommentDetailScreenArgs,
    viewModel: CommentDetailViewModel = hiltViewModel(),
    onCommentDeleted: () -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onShowTokensCreatedBy: (userUid: String) -> Unit,
    onOpenArtistDetailCalled: (userUid: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = CommentDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.commentDeleted) {
                    onCommentDeleted()
                } else {
                    value = it
                }
            }
        }
    }
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadDetail(uid = args.uid)
        }
        CommentDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onDeleteComment = ::deleteComment,
            onBackClicked = onBackClicked,
            onGoToTokenDetail = onGoToTokenDetail,
            onShowTokensCreatedBy = onShowTokensCreatedBy,
            onOpenArtistDetailCalled = onOpenArtistDetailCalled,
            onConfirmDeleteCommentDialogVisibilityChanged = ::onConfirmDeleteCommentDialogVisibilityChanged
        )
    }
}

@Composable
fun CommentDetailComponent(
    context: Context,
    uiState: CommentDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onBackClicked: () -> Unit,
    onOpenArtistDetailCalled: (userUid: String) -> Unit,
    onDeleteComment: (comment: Comment) -> Unit,
    onShowTokensCreatedBy: (userUid: String) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onConfirmDeleteCommentDialogVisibilityChanged: (isVisible: Boolean)  -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            onBackClicked = onBackClicked,
            imageUrl = comment?.user?.photoUrl,
            title = comment?.user?.name?.ifBlank {
                stringResource(id = R.string.search_user_info_name_empty)
            } ?: stringResource(id = R.string.search_user_info_name_empty)
        ) {
            val defaultModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
            ConfirmDeleteTokenDialog(
                uiState = uiState,
                onDeleteCommentCalled = onDeleteComment
            ) {
                onConfirmDeleteCommentDialogVisibilityChanged(false)
            }
            comment?.user?.let { userInfo ->
                userInfo.professionalTitle?.let {
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.TITLE_LARGE,
                        titleText = it
                    )
                }
                UserFollowersInfoComponent(
                    modifier = defaultModifier,
                    followersCount = userInfo.followers,
                    followingCount = userInfo.following
                )
                UserStatisticsComponent(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    itemSize = 30.dp,
                    userInfo = userInfo
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            CommonText(
                modifier = defaultModifier,
                type = CommonTextTypeEnum.TITLE_MEDIUM,
                titleText = comment?.createdAt?.format()?.let {
                    stringResource(id = R.string.comment_detail_created_at_label, it)
                },
                textColor = DarkPurple
            )
            CommonText(
                modifier = defaultModifier,
                type = CommonTextTypeEnum.BODY_LARGE,
                titleText = comment?.text
            )
            Spacer(modifier = Modifier.height(30.dp))
            ArtCollectiblesRow(
                context = context,
                reverseStyle = true,
                titleRes = R.string.comment_detail_tokens_created_by_user_text,
                items = tokensCreated,
                onShowAllItems = {
                    comment?.user?.uid?.let(onShowTokensCreatedBy)
                },
                onItemSelected = {
                    onGoToTokenDetail(it.id)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            CommonButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                text = R.string.comment_detail_artist_detail_button_text,
                containerColor = Purple700,
                contentColor = Color.White,
                onClick = {
                    comment?.user?.uid?.let(onOpenArtistDetailCalled)
                }
            )
            if(isDeleteCommentEnabled) {
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = R.string.comment_detail_delete_button_text,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    onClick = {
                        onConfirmDeleteCommentDialogVisibilityChanged(true)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ConfirmDeleteTokenDialog(
    uiState: CommentDetailUiState,
    onDeleteCommentCalled: (comment: Comment) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmDeleteCommentDialogVisible,
            titleRes = R.string.comment_detail_delete_comment_confirm_title_text,
            descriptionRes = R.string.comment_detail_delete_comment_confirm_description_text,
            acceptRes = R.string.comment_detail_delete_comment_confirm_accept_button_text,
            cancelRes = R.string.comment_detail_delete_comment_confirm_cancel_button_text,
            onAcceptClicked = {
                comment?.let(onDeleteCommentCalled)
            },
            onCancelClicked = onDialogCancelled
        )
    }
}