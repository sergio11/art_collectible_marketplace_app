package com.dreamsoftware.artcollectibles.ui.screens.comments


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.ui.components.UserAccountProfilePicture
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalColumnScreen
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.google.common.collect.Iterables
import java.math.BigInteger

data class CommentsScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun CommentsScreen(
    args: CommentsScreenArgs,
    viewModel: CommentsViewModel = hiltViewModel(),
    onGoToCommentDetail: (comment: Comment) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = CommentsUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load(args.tokenId)
        }
        CommentsComponent(
            state = uiState,
            snackBarHostState = snackBarHostState,
            lazyListState = lazyListState,
            onGoToCommentDetail = onGoToCommentDetail,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun CommentsComponent(
    state: CommentsUiState,
    lazyListState: LazyListState,
    snackBarHostState: SnackbarHostState,
    onGoToCommentDetail: (comment: Comment) -> Unit,
    onBackPressed: () -> Unit
) {
    with(state) {
        CommonVerticalColumnScreen(
            lazyListState = lazyListState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = comments,
            onBackPressed = onBackPressed,
            appBarTitle = getTopAppBarTitle(comments)
        ) { comment ->
            CommentItemDetail(
                modifier = Modifier.clickable {
                    onGoToCommentDetail(comment)
                },
                comment = comment
            )
        }
    }
}

@Composable
private fun CommentItemDetail(modifier: Modifier = Modifier, comment: Comment) {
    with(comment) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Purple200, RoundedCornerShape(percent = 30))
                .background(BackgroundWhite.copy(alpha = 0.9f), RoundedCornerShape(percent = 30))
                .padding(16.dp)
                .then(modifier)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAccountProfilePicture(size = 70.dp, userInfo = user)
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CommonText(
                        type = CommonTextTypeEnum.TITLE_MEDIUM,
                        titleText = user.name,
                        textColor = Color.Gray,
                        singleLine = true
                    )
                    user.professionalTitle?.let {
                        CommonText(
                            modifier = Modifier.padding(vertical = 2.dp),
                            type = CommonTextTypeEnum.LABEL_MEDIUM,
                            titleText = it,
                            singleLine = true,
                            textColor = Color.DarkGray
                        )
                    }
                    CommonText(
                        modifier = Modifier.padding(top = 4.dp),
                        type = CommonTextTypeEnum.BODY_MEDIUM,
                        titleText = text,
                        maxLines = 5
                    )
                }
            }
            CommonText(
                modifier = Modifier.align(Alignment.End),
                type = CommonTextTypeEnum.TITLE_SMALL,
                titleText = createdAt.format(),
                textColor = Color.Gray,
                singleLine = true
            )
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    data: Iterable<Comment>
) = if (Iterables.isEmpty(data)) {
    stringResource(id = R.string.comments_detail_title_default)
} else {
    stringResource(id = R.string.comments_detail_title_count, Iterables.size(data))
}