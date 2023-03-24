package com.dreamsoftware.artcollectibles.ui.screens.comments


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.UserAccountProfilePicture
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

data class CommentsScreenArgs(
    val tokenId: String
)

@Composable
fun CommentsScreen(
    args: CommentsScreenArgs,
    viewModel: CommentsViewModel = hiltViewModel(),
    onSeeCommentDetail: (comment: Comment) -> Unit
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
    val lazyListState = rememberLazyListState()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load(args.tokenId)
        }
        CommentsComponent(
            state = uiState,
            lazyListState = lazyListState,
            onSeeCommentDetail = onSeeCommentDetail
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentsComponent(
    state: CommentsUiState,
    lazyListState: LazyListState,
    onSeeCommentDetail: (comment: Comment) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            getTopAppBarTitle(comments),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Purple40
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column {
                    LazyColumn(
                        modifier = Modifier.padding(8.dp),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(Iterables.size(comments)) { index ->
                            val comment = Iterables.get(comments, index)
                            CommentItemDetail(
                                modifier = Modifier.clickable {
                                    onSeeCommentDetail(comment)
                                },
                                comment = comment
                            )
                        }
                    }
                }
            }
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
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(percent = 30))
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
                    Text(
                        text = user.name,
                        fontFamily = montserratFontFamily,
                        color = Color.Gray,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = text,
                        fontFamily = montserratFontFamily,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left
                    )
                }
            }
            Text(
                modifier = Modifier.align(Alignment.End),
                text = createdAt.format(),
                fontFamily = montserratFontFamily,
                color = Color.Gray,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left
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