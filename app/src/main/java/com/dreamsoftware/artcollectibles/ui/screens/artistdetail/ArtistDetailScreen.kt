package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.PurpleGrey80
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.math.BigInteger

data class ArtistDetailScreenArgs(
    val uid: String
)

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    args: ArtistDetailScreenArgs,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    onShowFollowers: (userInfo: UserInfo) -> Unit,
    onShowFollowing: (userInfo: UserInfo) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onShowTokensOwnedBy: (userInfo: UserInfo) -> Unit,
    onShowTokensCreatedBy: (userInfo: UserInfo) -> Unit,
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = ArtistDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadDetail(uid = args.uid)
        }
        ArtistDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onFollowUser = ::followUser,
            onUnfollowUser = ::unfollowUser,
            onShowFollowers = onShowFollowers,
            onShowFollowing = onShowFollowing,
            onGoToTokenDetail = onGoToTokenDetail,
            onShowTokensOwnedBy = onShowTokensOwnedBy,
            onShowTokensCreatedBy = onShowTokensCreatedBy
        )
    }
}

@Composable
fun ArtistDetailComponent(
    context: Context,
    uiState: ArtistDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onFollowUser: (userUid: String) -> Unit,
    onUnfollowUser: (userUid: String) -> Unit,
    onShowFollowers: (userInfo: UserInfo) -> Unit,
    onShowFollowing: (userInfo: UserInfo) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onShowTokensOwnedBy: (userInfo: UserInfo) -> Unit,
    onShowTokensCreatedBy: (userInfo: UserInfo) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = userInfo?.photoUrl,
            title = userInfo?.name?.ifBlank {
                stringResource(id = R.string.search_user_info_name_empty)
            } ?: stringResource(id = R.string.search_user_info_name_empty)
        ) {
            val defaultModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
            Box(
                modifier = defaultModifier
            ) {
                userInfo?.professionalTitle?.let {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = it,
                        fontFamily = montserratFontFamily,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (!isAuthUser) {
                    CommonButton(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .align(Alignment.CenterEnd),
                        text = if (isFollowing) {
                            R.string.profile_following_button_unfollow_text
                        } else {
                            R.string.profile_following_button_follow_text
                        },
                        widthDp = 120.dp,
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PurpleGrey80,
                            contentColor = Purple40
                        ),
                        buttonShape = ButtonDefaults.outlinedShape,
                        onClick = {
                            userInfo?.uid?.let {
                                if (isFollowing) {
                                    onUnfollowUser(it)
                                } else {
                                    onFollowUser(it)
                                }
                            }
                        }
                    )
                }
            }
            UserFollowersInfoComponent(
                modifier = defaultModifier,
                followersCount = followersCount,
                followingCount = followingCount,
                onShowFollowers = {
                    userInfo?.let(onShowFollowers)
                },
                onShowFollowing = {
                    userInfo?.let(onShowFollowing)
                }
            )
            UserStatisticsComponent(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                itemSize = 30.dp,
                userInfo = userInfo
            )
            userInfo?.tags?.let { tags ->
                if (tags.isNotEmpty()) {
                    TagsRow(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        tagList = tags,
                        isReadOnly = true
                    )
                }
            }
            Row(
                modifier = defaultModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                userInfo?.location?.let {
                    TextWithImage(
                        imageRes = R.drawable.user_location_icon,
                        text = it
                    )
                }
            }
            Row(
                modifier = defaultModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextWithImage(
                    imageRes = R.drawable.user_mail_icon,
                    text = userInfo?.contact ?: stringResource(id = R.string.no_text_value)
                )
                userInfo?.birthdate?.let {
                    TextWithImage(
                        imageRes = R.drawable.user_birthdate_icon,
                        text = it
                    )
                }
            }
            Text(
                modifier = defaultModifier,
                text = userInfo?.info?.let {
                    it.ifBlank {
                        stringResource(id = R.string.search_user_info_description_empty)
                    }
                } ?: stringResource(id = R.string.no_text_value),
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.bodyLarge
            )
            ArtCollectiblesRow(
                modifier = defaultModifier,
                context = context,
                reverseStyle = true,
                titleRes = R.string.profile_tokens_owned_by_user_text,
                items = tokensOwned,
                onShowAllItems = {
                    userInfo?.let(onShowTokensOwnedBy)
                },
                onItemSelected = {
                    onGoToTokenDetail(it.id)
                }
            )
            ArtCollectiblesRow(
                modifier = defaultModifier,
                context = context,
                reverseStyle = true,
                titleRes = R.string.profile_tokens_created_by_user_text,
                items = tokensCreated,
                onShowAllItems = {
                    userInfo?.let(onShowTokensCreatedBy)
                },
                onItemSelected = {
                    onGoToTokenDetail(it.id)
                }
            )
        }
    }
}