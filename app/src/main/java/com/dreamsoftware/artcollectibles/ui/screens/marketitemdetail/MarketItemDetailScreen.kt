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
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
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
            onWithdrawFromSaleCalled = ::withDrawFromSale,
            onOpenArtistDetailCalled = onOpenArtistDetailCalled,
            onExitCalled = onExitCalled
        )
    }
}

@Composable
fun MarketItemDetailComponent(
    context: Context,
    uiState: MarketUiState,
    scrollState: ScrollState,
    density: Density,
    onBuyItemCalled: (tokenId: BigInteger, price: BigInteger) -> Unit,
    onWithdrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onOpenArtistDetailCalled: (userInfo: UserInfo) -> Unit,
    onExitCalled: () -> Unit
) {
    with(uiState) {
        // =======================
        TokenWithdrawnFromSaleDialog(uiState, onExitCalled)
        TokenBoughtDialog(uiState, onExitCalled)
        // ========================
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = artCollectibleForSale?.token?.imageUrl,
            title = artCollectibleForSale?.token?.displayName
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                UserMiniInfoComponent(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            artCollectibleForSale?.seller?.let {
                                onOpenArtistDetailCalled(it)
                            }
                        },
                    artCollectibleForSale?.seller
                )
                MarketItemPriceRow(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    artCollectibleForSale?.price)
            }
            TokenDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                artCollectibleForSale?.token
            )
            if (!isLoading) {
                if(!isTokenSeller) {
                    CommonButton(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.market_item_detail_buy_item_button_text,
                        onClick = {
                            artCollectibleForSale?.let {
                                onBuyItemCalled(it.token.id, it.price)
                            }
                        }
                    )
                }
                if(isTokenAuthor) {
                    CommonButton(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 8.dp)
                            .width(300.dp),
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
            }
        }
    }
}

@Composable
private fun MarketItemPriceRow(modifier: Modifier, price: BigInteger?) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MaticIconComponent(size = 20.dp)
        Text(
            text = price?.toString() ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TokenDetail(modifier: Modifier, artCollectible: ArtCollectible?) {
    Column(modifier = modifier) {
        Text(
            text = artCollectible?.name ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row {
            TokenCreatorInfoComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible?.author
            )
            TokenRoyaltyComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible?.royalty
            )
        }

        Text(
            text = artCollectible?.description ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
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