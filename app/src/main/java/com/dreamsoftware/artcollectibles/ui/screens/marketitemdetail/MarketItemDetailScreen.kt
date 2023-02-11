package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
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
    onItemBought: () -> Unit
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
                if (it.itemBought) {
                    onItemBought()
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
        MarketItemDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onBuyItemClicked = ::buyItem
        )
    }
}

@Composable
fun MarketItemDetailComponent(
    context: Context,
    uiState: MarketUiState,
    scrollState: ScrollState,
    density: Density,
    onBuyItemClicked: (tokenId: BigInteger, price: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = artCollectibleForSale?.token?.imageUrl,
            title = artCollectibleForSale?.token?.displayName
        ) {
            Box {
                UserMiniInfoComponent(
                    modifier = Modifier.align(Alignment.CenterStart),
                    artCollectibleForSale?.seller
                )
                MarketItemPriceRow(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    artCollectibleForSale?.price)
            }
            TokenDetail(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                artCollectibleForSale?.token
            )
            if (!isLoading && !isTokenSeller) {
                CommonButton(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 8.dp)
                        .width(300.dp),
                    text = R.string.market_item_detail_buy_item_button_text,
                    onClick = {
                        artCollectibleForSale?.let {
                            onBuyItemClicked(it.token.id, it.price)
                        }
                    }
                )
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
        TokenRoyaltyComponent(
            modifier = Modifier.padding(8.dp),
            artCollectible?.royalty
        )
        Text(
            text = artCollectible?.description ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}