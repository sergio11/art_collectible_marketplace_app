package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import java.math.BigInteger

private const val PRICE_NUMBER_OF_DECIMALS = 5

data class TokenDetailScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenDetailScreen(
    args: TokenDetailScreenArgs,
    viewModel: TokenDetailViewModel = hiltViewModel(),
    onSeeArtistDetail: (userInfo: UserInfo) -> Unit,
    onTokenBurned: () -> Unit,
    onSeeCommentDetail: (comment: Comment) -> Unit,
    onSeeCommentsByToken: (tokenId: BigInteger) -> Unit,
    onSeeLikesByToken: (tokenId: BigInteger) -> Unit,
    onSeeVisitorsByToken: (tokenId: BigInteger) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = TokenDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.isBurned) {
                    onTokenBurned()
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
            loadDetail(tokenId = args.tokenId)
        }
        TokenDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onSeeArtistDetail = onSeeArtistDetail,
            onBurnTokenCalled = ::burnToken,
            onPutItemForSaleCalled = ::putItemForSale,
            onWithDrawFromSaleCalled = ::withDrawFromSale,
            onItemPriceChanged = ::onTokenPriceChanged,
            onTokenAddedToFavorites = ::addTokenToFavorites,
            onTokenRemovedFromFavorites = ::removeTokenFromFavorites,
            onConfirmBurnTokenDialogVisibilityChanged = ::onConfirmBurnTokenDialogVisibilityChanged,
            onConfirmWithDrawFromSaleDialogVisibilityChanged = ::onConfirmWithDrawFromSaleDialogVisibilityChanged,
            onConfirmPutForSaleDialogVisibilityChanged = ::onConfirmPutForSaleDialogVisibilityChanged,
            onPublishComment = ::onPublishComment,
            onSeeCommentDetail = onSeeCommentDetail,
            onSeeCommentsByToken = onSeeCommentsByToken,
            onSeeLikesByToken = onSeeLikesByToken,
            onSeeVisitorsByToken = onSeeVisitorsByToken
        )
    }
}

@Composable
fun TokenDetailComponent(
    context: Context,
    uiState: TokenDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onSeeArtistDetail: (userInfo: UserInfo) -> Unit,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onTokenAddedToFavorites: (tokenId: BigInteger) -> Unit,
    onTokenRemovedFromFavorites: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onItemPriceChanged: (price: String) -> Unit,
    onPublishComment: (comment: String) -> Unit,
    onSeeCommentDetail: (comment: Comment) -> Unit,
    onSeeCommentsByToken: (tokenId: BigInteger) -> Unit,
    onSeeLikesByToken: (tokenId: BigInteger) -> Unit,
    onSeeVisitorsByToken: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = artCollectible?.metadata?.imageUrl,
            title = artCollectible?.displayName
        ) {
            TokenDetailBody(
                uiState = uiState,
                onSeeArtistDetail = onSeeArtistDetail,
                onBurnTokenCalled = onBurnTokenCalled,
                onWithDrawFromSaleCalled = onWithDrawFromSaleCalled,
                onPutItemForSaleCalled = onPutItemForSaleCalled,
                onItemPriceChanged = onItemPriceChanged,
                onTokenAddedToFavorites = onTokenAddedToFavorites,
                onTokenRemovedFromFavorites = onTokenRemovedFromFavorites,
                onConfirmBurnTokenDialogVisibilityChanged = onConfirmBurnTokenDialogVisibilityChanged,
                onConfirmWithDrawFromSaleDialogVisibilityChanged = onConfirmWithDrawFromSaleDialogVisibilityChanged,
                onConfirmPutForSaleDialogVisibilityChanged = onConfirmPutForSaleDialogVisibilityChanged,
                onPublishComment = onPublishComment,
                onSeeCommentDetail = onSeeCommentDetail,
                onSeeAllComments = onSeeCommentsByToken,
                onSeeLikesByToken = onSeeLikesByToken,
                onSeeVisitorsByToken = onSeeVisitorsByToken
            )
        }
    }
}


@Composable
private fun TokenDetailBody(
    uiState: TokenDetailUiState,
    onSeeArtistDetail: (userInfo: UserInfo) -> Unit,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onItemPriceChanged: (price: String) -> Unit,
    onTokenAddedToFavorites: (tokenId: BigInteger) -> Unit,
    onTokenRemovedFromFavorites: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onPublishComment: (comment: String) -> Unit,
    onSeeCommentDetail: (comment: Comment) -> Unit,
    onSeeAllComments: (tokenId: BigInteger) -> Unit,
    onSeeLikesByToken: (tokenId: BigInteger) -> Unit,
    onSeeVisitorsByToken: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        artCollectible?.let { artCollectible ->
            //.......................................................................
            ConfirmBurnTokenDialog(uiState, onBurnTokenCalled) {
                onConfirmBurnTokenDialogVisibilityChanged(false)
            }
            ConfirmWithdrawFromSaleDialog(uiState, onWithDrawFromSaleCalled) {
                onConfirmWithDrawFromSaleDialogVisibilityChanged(false)
            }
            ConfirmPutItemForSaleDialog(uiState, onPutItemForSaleCalled, onItemPriceChanged) {
                onConfirmPutForSaleDialogVisibilityChanged(false)
            }
            //.......................................................................
            Box(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                UserMiniInfoComponent(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            onSeeArtistDetail(artCollectible.author)
                        },
                    userInfo = artCollectible.author
                )
                FavoriteButton(
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.CenterEnd),
                    isChecked = tokenAddedToFavorites,
                    onCheckedChange = {
                        if(tokenAddedToFavorites) {
                            onTokenRemovedFromFavorites(artCollectible.id)
                        } else {
                            onTokenAddedToFavorites(artCollectible.id)
                        }
                    }
                )
            }
            TagsRow(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                tagList = artCollectible.metadata.tags,
                isReadOnly = true
            )
            ArtCollectibleMiniInfoComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                artCollectible = artCollectible,
                onSeeAllComments = onSeeAllComments,
                onSeeLikesByToken = onSeeLikesByToken,
                onSeeVisitorsByToken = onSeeVisitorsByToken,
                onSeeCreatorDetail = onSeeArtistDetail
            )
            Spacer(modifier = Modifier.height(30.dp))
            PublishCommentComponent(
                modifier = Modifier.padding(16.dp),
                authUserInfo = authUserInfo,
                commentsCount = artCollectible.commentsCount,
                lastComments = lastComments,
                onPublishComment = onPublishComment,
                onSeeCommentDetail = onSeeCommentDetail,
                onSeeAllComments = {
                    onSeeAllComments(artCollectible.id)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (isTokenOwner) {
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = if (isTokenAddedForSale) {
                        R.string.token_detail_with_draw_from_sale_button_text
                    } else {
                        R.string.token_detail_put_item_for_sale_button_text
                    },
                    onClick = {
                        if (isTokenAddedForSale) {
                            onConfirmWithDrawFromSaleDialogVisibilityChanged(true)
                        } else {
                            onConfirmPutForSaleDialogVisibilityChanged(true)
                        }
                    }
                )
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = R.string.token_detail_burn_token_button_text,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    onClick = {
                        onConfirmBurnTokenDialogVisibilityChanged(true)
                    }
                )
            }
        }
    }
}


@Composable
private fun ConfirmWithdrawFromSaleDialog(
    uiState: TokenDetailUiState,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmWithDrawFromSaleDialogVisible,
            titleRes = R.string.token_detail_with_draw_from_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_with_draw_from_sale_dialog_description_text,
            acceptRes = R.string.token_detail_with_draw_from_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_with_draw_from_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onWithDrawFromSaleCalled(it.id)
                }
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun ConfirmBurnTokenDialog(
    uiState: TokenDetailUiState,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmBurnTokenDialogVisible,
            titleRes = R.string.token_detail_burn_token_confirm_title_text,
            descriptionRes = R.string.token_detail_burn_token_confirm_description_text,
            acceptRes = R.string.token_detail_burn_token_confirm_accept_button_text,
            cancelRes = R.string.token_detail_burn_token_confirm_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onBurnTokenCalled(it.id)
                }
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun ConfirmPutItemForSaleDialog(
    uiState: TokenDetailUiState,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onItemPriceChanged: (price: String) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmPutForSaleDialogVisible,
            titleRes = R.string.token_detail_put_item_for_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_put_item_for_sale_dialog_description_text,
            acceptRes = R.string.token_detail_put_item_for_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_put_item_for_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onPutItemForSaleCalled(it.id)
                }
            },
            isAcceptEnabled = isPutTokenForSaleConfirmButtonEnabled,
            onCancelClicked = onDialogCancelled
        ) {
            CommonDefaultDecimalField(
                modifier = Modifier.padding(horizontal = 8.dp),
                labelRes = R.string.token_detail_put_item_for_sale_input_price_label,
                placeHolderRes = R.string.token_detail_put_item_for_sale_input_price_placeholder,
                value = tokenPrice,
                numberOfDecimals = PRICE_NUMBER_OF_DECIMALS,
                leadingIcon = {
                    MaticIconComponent(size = 20.dp)
                },
                onValueChanged = {
                    onItemPriceChanged(it)
                }
            )
        }
    }
}
