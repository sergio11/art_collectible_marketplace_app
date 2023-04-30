package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.BuildConfig
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.CommonDetailScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.PurpleGrey80
import com.dreamsoftware.artcollectibles.ui.theme.instagramPurpleRed
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.math.BigInteger

data class ArtistDetailScreenArgs(
    val uid: String
)

@Composable
fun ArtistDetailScreen(
    args: ArtistDetailScreenArgs,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    onShowFollowers: (userUid: String) -> Unit,
    onShowFollowing: (userUid: String) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onShowTokensOwnedBy: (userUid: String) -> Unit,
    onShowTokensCreatedBy: (userUid: String) -> Unit,
    onBackClicked: () -> Unit
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
            onBackClicked = onBackClicked,
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
    onBackClicked: () -> Unit,
    onFollowUser: (userUid: String) -> Unit,
    onUnfollowUser: (userUid: String) -> Unit,
    onShowFollowers: (userUid: String) -> Unit,
    onShowFollowing: (userUid: String) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onShowTokensOwnedBy: (userUid: String) -> Unit,
    onShowTokensCreatedBy: (userUid: String) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = userInfo?.photoUrl,
            onBackClicked = onBackClicked,
            title = userInfo?.name?.ifBlank {
                stringResource(id = R.string.search_user_info_name_empty)
            } ?: stringResource(id = R.string.search_user_info_name_empty)
        ) {
            val defaultModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
            Box(
                modifier = defaultModifier.fillMaxWidth()
            ) {
                userInfo?.professionalTitle?.let {
                    CommonText(
                        modifier = Modifier.align(Alignment.CenterStart),
                        type = CommonTextTypeEnum.TITLE_LARGE,
                        titleText = it
                    )
                }
                if (!isAuthUser) {
                    CommonButton(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterEnd),
                        text = if (isFollowing) {
                            R.string.profile_following_button_unfollow_text
                        } else {
                            R.string.profile_following_button_follow_text
                        },
                        textType = CommonTextTypeEnum.LABEL_MEDIUM,
                        widthDp = 110.dp,
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
                modifier = defaultModifier.fillMaxWidth(),
                followersCount = followersCount,
                followingCount = followingCount,
                onShowFollowers = {
                    userInfo?.uid?.let(onShowFollowers)
                },
                onShowFollowing = {
                    userInfo?.uid?.let(onShowFollowing)
                }
            )
            UserStatisticsComponent(
                modifier = defaultModifier,
                itemSize = 30.dp,
                userInfo = userInfo
            )
            FlowRow(
                modifier = defaultModifier,
                crossAxisAlignment = FlowCrossAxisAlignment.Center
            ) {
                userInfo?.location?.let {
                    TextWithImage(
                        modifier = Modifier.padding(8.dp),
                        imageRes = R.drawable.user_location_icon,
                        text = it
                    )
                }
                userInfo?.country?.let {
                    TextWithImage(
                        modifier = Modifier.padding(8.dp),
                        imageRes = R.drawable.country_icon,
                        text = it
                    )
                }
                TextWithImage(
                    modifier = Modifier.padding(8.dp),
                    imageRes = R.drawable.user_mail_icon,
                    text = userInfo?.contact ?: stringResource(id = R.string.no_text_value)
                )
                userInfo?.birthdate?.let {
                    TextWithImage(
                        modifier = Modifier.padding(8.dp),
                        imageRes = R.drawable.user_birthdate_icon,
                        text = it
                    )
                }
                userInfo?.instagramNick?.let {
                    TextWithImage(
                        modifier = Modifier.padding(8.dp),
                        imageRes = R.drawable.instagram_icon,
                        text = it
                    )
                }
            }
            CommonText(
                modifier = defaultModifier,
                type = CommonTextTypeEnum.BODY_LARGE,
                titleText = userInfo?.info?.let {
                    it.ifBlank {
                        stringResource(id = R.string.search_user_info_description_empty)
                    }
                }
            )
            userInfo?.tags?.let { tags ->
                if (tags.isNotEmpty()) {
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.TITLE_LARGE,
                        titleRes = R.string.profile_tokens_artist_interested_in,
                        textColor = DarkPurple,
                        maxLines = 2
                    )
                    TagsRow(
                        modifier = defaultModifier,
                        tagList = tags,
                        isReadOnly = true
                    )
                }
            }
            currentBalance?.let {
                CommonText(
                    modifier = defaultModifier,
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleRes = R.string.profile_tokens_artist_account_balance,
                    textColor = DarkPurple,
                    maxLines = 2
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
                    userInfo?.uid?.let(onShowTokensOwnedBy)
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
                    userInfo?.uid?.let(onShowTokensCreatedBy)
                },
                onItemSelected = {
                    onGoToTokenDetail(it.id)
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            userInfo?.instagramNick?.let { nick ->
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = R.string.profile_open_instagram_profile,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = instagramPurpleRed,
                        contentColor = Color.White
                    ),
                    buttonShape = ButtonDefaults.elevatedShape,
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(BuildConfig.INSTAGRAM_URL + nick)
                            )
                        )
                    }
                )
            }
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
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}