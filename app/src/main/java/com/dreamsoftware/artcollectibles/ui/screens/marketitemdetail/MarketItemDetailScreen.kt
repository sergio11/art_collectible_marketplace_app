package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.math.BigInteger

data class MarketItemDetailScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun MarketItemDetailScreen(
    args: MarketItemDetailScreenArgs,
    viewModel: MarketItemDetailViewModel = hiltViewModel(),
    onOpenArtistDetailCalled: (userInfo: UserInfo) -> Unit,
    onOpenTokenDetailCalled: (tokenId: BigInteger) -> Unit,
    onOpenMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onExitCalled: () -> Unit
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
            loadDetail(tokenId = args.tokenId)
        }
        MarketItemDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onBuyItemCalled = ::buyItem,
            onConfirmBuyItemDialogVisibilityChanged = ::onConfirmBuyItemDialogVisibilityChanged,
            onWithdrawFromSaleCalled = ::withDrawFromSale,
            onOpenArtistDetailCalled = onOpenArtistDetailCalled,
            onExitCalled = onExitCalled,
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
    onBuyItemCalled: (tokenId: BigInteger, price: BigInteger) -> Unit,
    onOpenTokenDetailCalled: (tokenId: BigInteger) -> Unit,
    onWithdrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onOpenArtistDetailCalled: (userInfo: UserInfo) -> Unit,
    onOpenMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onExitCalled: () -> Unit
) {
    with(uiState) {
        // =======================
        TokenWithdrawnFromSaleDialog(uiState, onExitCalled)
        TokenBoughtDialog(uiState, onExitCalled)
        ConfirmBuyItemDialog(uiState, onBuyItemCalled, onExitCalled)
        // ========================
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
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
                            artCollectibleForSale?.seller?.let {
                                onOpenArtistDetailCalled(it)
                            }
                        },
                    userInfo = artCollectibleForSale?.seller
                )
                MarketItemPriceRow(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    price = artCollectibleForSale?.price
                )
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
            Spacer(modifier = Modifier.height(50.dp))
            if (!isLoading) {
                if (!isTokenSeller) {
                    CommonButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple200,
                            contentColor = Color.White
                        ),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple700,
                        contentColor = Color.White
                    ),
                    onClick = {
                        artCollectibleForSale?.token?.id?.let {
                            onOpenTokenDetailCalled(it)
                        }
                    }
                )
                if (isTokenAuthor) {
                    CommonButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        text = R.string.market_item_detail_withdraw_from_sale_button_text,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = {
                            artCollectibleForSale?.let {
                                onWithdrawFromSaleCalled(it.token.id)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun MarketItemPriceRow(modifier: Modifier = Modifier, price: BigInteger?) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaticIconComponent(size = 35.dp)
        Text(
            text = price?.toString() ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
    onConfirmBuyItemCalled: (tokenId: BigInteger, price: BigInteger) -> Unit,
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
                    onConfirmBuyItemCalled(it.token.id, it.price)
                }
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