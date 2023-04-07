package com.dreamsoftware.artcollectibles.ui.screens.commentdetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
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
    onShowTokensCreatedBy: (userInfo: UserInfo) -> Unit,
    onOpenArtistDetailCalled: (userInfo: UserInfo) -> Unit
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
            onGoToTokenDetail = onGoToTokenDetail,
            onShowTokensCreatedBy = onShowTokensCreatedBy,
            onOpenArtistDetailCalled = onOpenArtistDetailCalled
        )
    }
}

@Composable
fun CommentDetailComponent(
    context: Context,
    uiState: CommentDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onOpenArtistDetailCalled: (userInfo: UserInfo) -> Unit,
    onDeleteComment: (comment: Comment) -> Unit,
    onShowTokensCreatedBy: (userInfo: UserInfo) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = comment?.user?.photoUrl,
            title = comment?.user?.name?.ifBlank {
                stringResource(id = R.string.search_user_info_name_empty)
            } ?: stringResource(id = R.string.search_user_info_name_empty)
        ) {
            val defaultModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
            comment?.user?.let { userInfo ->
                userInfo.professionalTitle?.let {
                    Text(
                        modifier = defaultModifier,
                        text = it,
                        fontFamily = montserratFontFamily,
                        style = MaterialTheme.typography.titleLarge
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
            Text(
                modifier = defaultModifier,
                text = comment?.text ?: stringResource(id = R.string.no_text_value),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = montserratFontFamily
            )
            Spacer(modifier = Modifier.height(30.dp))
            ArtCollectiblesRow(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                context = context,
                reverseStyle = true,
                titleRes = R.string.comment_detail_tokens_created_by_user_text,
                items = tokensCreated,
                onShowAllItems = {
                    comment?.user?.let(onShowTokensCreatedBy)
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple700,
                    contentColor = Color.White
                ),
                onClick = {
                    comment?.user?.let(onOpenArtistDetailCalled)
                }
            )
            CommonButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                text = R.string.comment_detail_delete_button_text,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                onClick = {
                    comment?.let(onDeleteComment)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}