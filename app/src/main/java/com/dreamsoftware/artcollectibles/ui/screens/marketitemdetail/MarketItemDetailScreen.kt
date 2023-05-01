package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.data.api.exception.FetchItemForSaleException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.ItemNotAvailableForSale
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleForSaleRow
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniInfoComponent
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectiblePrice
import com.dreamsoftware.artcollectibles.ui.components.UserMiniInfoComponent
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import java.math.BigInteger

data class MarketItemDetailScreenArgs(
    val id: BigInteger,
    val viewType: ViewTypeEnum
) {
    enum class ViewTypeEnum {
        HISTORY, FOR_SALE
    }
}

@Composable
fun MarketItemDetailScreen(
    args: MarketItemDetailScreenArgs,
    viewModel: MarketItemDetailViewModel = hiltViewModel(),
    onOpenArtistDetailCalled: (userUid: String) -> Unit,
    onOpenTokenDetailCalled: (tokenId: BigInteger) -> Unit,
    onOpenMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = MarketUiState(),
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
            with(args) {
                if(viewType == MarketItemDetailScreenArgs.ViewTypeEnum.HISTORY) {
                    loadMarketHistoryItemDetail(id)
                } else {
                    loadForSaleMarketItemDetail(id)
                }
            }
        }
        MarketItemDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onBuyItemCalled = ::buyItem,
            onConfirmBuyItemDialogVisibilityChanged = ::onConfirmBuyItemDialogVisibilityChanged,
            onConfirmWithDrawFromSaleDialogVisibilityChanged = ::onConfirmWithDrawFromSaleDialogVisibilityChanged,
            onWithdrawFromSaleCalled = ::withDrawFromSale,
            onOpenArtistDetailCalled = onOpenArtistDetailCalled,
            onBackClicked = onBackClicked,
            onOpenTokenDetailCalled = onOpenTokenDetailCalled,
            onOpenMarketItemDetail = onOpenMarketItemDetail
        )
    }
}

@Composable
fun MarketItemDetailComponent(
    context: Context,
    uiState: MarketUiState,
    scrollState: ScrollState,
    density: Density,
    onConfirmBuyItemDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onBuyItemCalled: (tokenId: BigInteger) -> Unit,
    onOpenTokenDetailCalled: (tokenId: BigInteger) -> Unit,
    onWithdrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onOpenArtistDetailCalled: (userUid: String) -> Unit,
    onOpenMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onBackClicked: () -> Unit
) {
    with(uiState) {
        // =======================
        TokenWithdrawnFromSaleDialog(uiState, onBackClicked)
        TokenBoughtDialog(uiState, onBackClicked)
        ConfirmBuyItemDialog(uiState, onBuyItemCalled, onBackClicked)
        TokenNotAvailableForSaleDialog(uiState, onBackClicked)
        ConfirmWithdrawFromSaleDialog(uiState, onWithdrawFromSaleCalled) {
            onConfirmWithDrawFromSaleDialogVisibilityChanged(false)
        }
        // ========================
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            onBackClicked = onBackClicked,
            imageUrl = artCollectibleForSale?.token?.metadata?.imageUrl,
            title = artCollectibleForSale?.token?.displayName
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                UserMiniInfoComponent(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            artCollectibleForSale?.seller?.uid?.let {
                                onOpenArtistDetailCalled(it)
                            }
                        },
                    userInfo = artCollectibleForSale?.seller
                )
                ArtCollectiblePrice(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .align(Alignment.CenterEnd),
                    iconSize = 30.dp,
                    textSize = 20.sp,
                    textColor = DarkPurple,
                    priceData = artCollectibleForSale?.price
                )
            }
            artCollectibleForSale?.let {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(10.dp),
                        painter = painterResource(
                            id = if (it.sold) {
                                R.drawable.sold_market_items
                            } else if (it.canceled) {
                                R.drawable.cancelled_market_items
                            } else {
                                R.drawable.available_market_items
                            }
                        ),
                        colorFilter = ColorFilter.tint(
                            if (it.sold) {
                                Color.Green
                            } else if (it.canceled) {
                                Color.Red
                            } else {
                                Purple500
                            }
                        ),
                        contentDescription = "Token Image"
                    )
                    Column {
                        CommonText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            type = CommonTextTypeEnum.TITLE_MEDIUM,
                            titleText = if (it.sold) {
                                stringResource(id = R.string.market_item_detail_sold_at_label, it.soldAt?.format().orEmpty())
                            } else if (it.canceled) {
                                stringResource(id = R.string.market_item_detail_canceled_at_label, it.canceledAt?.format().orEmpty())
                            } else {
                                stringResource(id = R.string.market_item_detail_put_for_sale_at_label, it.putForSaleAt.format())
                            },
                            textColor = DarkPurple
                        )
                        ArtCollectiblePrice(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            iconSize = 20.dp,
                            textSize = 20.sp,
                            textColor = DarkPurple,
                            fullMode = true,
                            priceData = it.price
                        )
                    }
                }
            }
            ArtCollectibleMiniInfoComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                artCollectible = artCollectibleForSale?.token
            )
            ArtCollectibleForSaleRow(
                context = context,
                titleRes = R.string.market_item_detail_similar_row_title_text,
                items = similarMarketItems,
                reverseStyle = true,
                onMarketItemSelected = onOpenMarketItemDetail
            )
            ArtCollectibleForSaleRow(
                context = context,
                titleRes = R.string.market_item_detail_similar_author_row_title_text,
                items = similarAuthorMarketItems,
                reverseStyle = true,
                onMarketItemSelected = onOpenMarketItemDetail
            )
            Spacer(modifier = Modifier.height(50.dp))
            if (!isLoading) {
                if (!isMarketHistoryItem && !isTokenSeller) {
                    if(!enoughFunds) {
                        CommonText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            type = CommonTextTypeEnum.TITLE_MEDIUM,
                            titleRes = R.string.market_item_detail_not_enough_funds_text,
                            textColor = DarkPurple
                        )
                    }
                    CommonButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        containerColor = Purple200,
                        contentColor = Color.White,
                        enabled = enoughFunds,
                        text = R.string.market_item_detail_buy_item_button_text,
                        onClick = {
                            onConfirmBuyItemDialogVisibilityChanged(true)
                        }
                    )
                }
                CommonButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    text = R.string.market_item_detail_open_item_button_text,
                    containerColor = Purple700,
                    contentColor = Color.White,
                    onClick = {
                        artCollectibleForSale?.token?.id?.let {
                            onOpenTokenDetailCalled(it)
                        }
                    }
                )
                if (!isMarketHistoryItem && isTokenAuthor) {
                    CommonButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        text = R.string.market_item_detail_withdraw_from_sale_button_text,
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        onClick = {
                            onConfirmWithDrawFromSaleDialogVisibilityChanged(true)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TokenWithdrawnFromSaleDialog(
    uiState: MarketUiState,
    onConfirmCalled: () -> Unit,
) {
    with(uiState) {
        CommonDialog(
            isVisible = itemWithdrawnFromSale,
            titleRes = R.string.market_item_detail_token_withdrawn_from_sale_title_text,
            descriptionRes = R.string.market_item_detail_token_withdrawn_from_sale_description_text,
            acceptRes = R.string.market_item_detail_token_withdrawn_from_sale_accept_button_text,
            onAcceptClicked = onConfirmCalled
        )
    }
}

@Composable
private fun ConfirmBuyItemDialog(
    uiState: MarketUiState,
    onConfirmBuyItemCalled: (tokenId: BigInteger) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmBuyItemDialogVisible,
            titleRes = R.string.market_item_detail_token_buy_confirm_title_text,
            descriptionRes = R.string.market_item_detail_token_buy_confirm_description_text,
            acceptRes = R.string.market_item_detail_token_buy_confirm_accept_button_text,
            cancelRes = R.string.market_item_detail_token_buy_confirm_cancel_button_text,
            onAcceptClicked = {
                artCollectibleForSale?.let {
                    onConfirmBuyItemCalled(it.token.id)
                }
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun ConfirmWithdrawFromSaleDialog(
    uiState: MarketUiState,
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
                artCollectibleForSale?.token?.id?.let(onWithDrawFromSaleCalled)
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun TokenBoughtDialog(
    uiState: MarketUiState,
    onConfirmCalled: () -> Unit,
) {
    with(uiState) {
        CommonDialog(
            isVisible = itemBought,
            titleRes = R.string.market_item_detail_token_bought_title_text,
            descriptionRes = R.string.market_item_detail_token_bought_description_text,
            acceptRes = R.string.market_item_detail_token_bought_accept_button_text,
            onAcceptClicked = onConfirmCalled
        )
    }
}

@Composable
private fun TokenNotAvailableForSaleDialog(
    uiState: MarketUiState,
    onConfirmCalled: () -> Unit,
) {
    with(uiState) {
        CommonDialog(
            isVisible = error is FetchItemForSaleException || error?.cause is ItemNotAvailableForSale,
            titleRes = R.string.market_item_detail_token_unavailable_title_text,
            descriptionRes = R.string.market_item_detail_token_unavailable_description_text,
            acceptRes = R.string.market_item_detail_token_bought_accept_button_text,
            onAcceptClicked = onConfirmCalled
        )
    }
}