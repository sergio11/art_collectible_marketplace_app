package com.dreamsoftware.artcollectibles.ui.screens.notifications


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
import com.dreamsoftware.artcollectibles.domain.models.Notification
import com.dreamsoftware.artcollectibles.ui.components.UserAccountProfilePicture
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalColumnScreen
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.google.common.collect.Iterables

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    onGoToNotificationDetail: (notification: Notification) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = NotificationsUiState(),
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
            load()
        }
        CommentsComponent(
            state = uiState,
            snackBarHostState = snackBarHostState,
            lazyListState = lazyListState,
            onGoToNotificationDetail = onGoToNotificationDetail,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun CommentsComponent(
    state: NotificationsUiState,
    snackBarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    onBackPressed: () -> Unit,
    onGoToNotificationDetail: (notification: Notification) -> Unit
) {
    with(state) {
        CommonVerticalColumnScreen(
            lazyListState = lazyListState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = notifications,
            onBackPressed = onBackPressed,
            appBarTitle = getTopAppBarTitle(notifications)
        ) { notification ->
            NotificationItemDetail(
                modifier = Modifier.clickable {
                    onGoToNotificationDetail(notification)
                },
                notification = notification
            )
        }
    }
}

@Composable
private fun NotificationItemDetail(modifier: Modifier = Modifier, notification: Notification) {
    with(notification) {
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
                    /*CommonText(
                        modifier = Modifier.padding(top = 4.dp),
                        type = CommonTextTypeEnum.BODY_MEDIUM,
                        titleText = ,
                        maxLines = 5
                    )*/
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
    data: Iterable<Notification>
) = if (Iterables.isEmpty(data)) {
    stringResource(id = R.string.notifications_detail_title_default)
} else {
    stringResource(id = R.string.notifications_detail_title_count, Iterables.size(data))
}