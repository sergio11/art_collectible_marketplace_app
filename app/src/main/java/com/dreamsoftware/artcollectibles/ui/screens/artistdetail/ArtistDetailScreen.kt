package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.BuildConfig
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
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
                userInfo?.country?.let {
                    TextWithImage(
                        imageRes = R.drawable.country_icon,
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
            userInfo?.tags?.let { tags ->
                if (tags.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.profile_tokens_artist_interested_in),
                        color = DarkPurple,
                        modifier = defaultModifier,
                        fontFamily = montserratFontFamily,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.titleLarge
                    )
                    TagsRow(
                        modifier = defaultModifier,
                        tagList = tags,
                        isReadOnly = true
                    )
                }
            }
            currentBalance?.let {
                Text(
                    text = stringResource(id = R.string.profile_tokens_artist_account_balance),
                    color = DarkPurple,
                    modifier = defaultModifier,
                    fontFamily = montserratFontFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.titleLarge
                )
                CurrentAccountBalance(
                    modifier = defaultModifier,
                    iconSize = 25.dp,
                    textSize = 20.sp,
                    textColor = DarkPurple,
                    accountBalance = it,
                    fullMode = true
                )
            }
            ArtCollectiblesRow(
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
            Spacer(modifier = Modifier.height(50.dp))
            if(isAuthUser) {
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = R.string.profile_get_more_matic,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = Color.White
                    ),
                    buttonShape = ButtonDefaults.elevatedShape,
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(BuildConfig.MUMBAI_FAUCET_URL)
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))
            }

        }
    }
}