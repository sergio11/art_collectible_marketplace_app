package com.dreamsoftware.artcollectibles.ui.screens.home

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTopAppBar
import com.dreamsoftware.artcollectibles.ui.theme.*
import com.google.common.collect.Iterables
import java.math.BigInteger

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToCategoryDetail: (category: ArtCollectibleCategory) -> Unit,
    onGoToUserDetail: (userInfo: UserInfo) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = HomeUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadData()
        }
        HomeComponent(
            navController = navController,
            context = context,
            uiState = uiState,
            onGoToMarketItemDetail = onGoToMarketItemDetail,
            onGoToCategoryDetail = onGoToCategoryDetail,
            onGoToUserDetail = onGoToUserDetail,
            onGoToTokenDetail = onGoToTokenDetail
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeComponent(
    navController: NavController,
    context: Context,
    uiState: HomeUiState,
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToCategoryDetail: (category: ArtCollectibleCategory) -> Unit,
    onGoToUserDetail: (userInfo: UserInfo) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            bottomBar = {
                BottomBar(navController)
            },
            topBar = {
                CommonTopAppBar(R.string.home_main_title)
            },
            containerColor = Purple40
        ) { paddingValues ->
            ScreenBackgroundImage(imageRes = R.drawable.screen_background_2)
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                marketplaceStatistics?.let {
                    MarketStatisticsRow(it)
                }
                if(!Iterables.isEmpty(categories)) {
                    ArtCollectibleCategoryList(
                        context = context,
                        titleRes = R.string.home_collectibles_categories_title,
                        categories = categories,
                        onCategoryClicked = onGoToCategoryDetail
                    )
                }
                if(!Iterables.isEmpty(moreFollowedUsers)) {
                    UserInfoArtistList(
                        context = context,
                        titleRes = R.string.home_featured_artists_title,
                        userList = moreFollowedUsers,
                        onUserClicked = onGoToUserDetail
                    )
                }
                ArtCollectiblesRow(
                    context = context,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                    titleRes = R.string.home_art_collectibles_featured_title,
                    items = moreLikedTokens,
                    onItemSelected = {
                        onGoToTokenDetail(it.id)
                    }
                )
                MarketplaceRow(context, R.string.home_available_items_for_sale_title, availableMarketItems, onGoToMarketItemDetail)
                MarketplaceRow(context, R.string.home_your_items_for_sale_title, sellingMarketItems, onGoToMarketItemDetail)
                MarketplaceRow(context, R.string.home_last_market_history_title, marketHistory, onGoToMarketItemDetail)
            }
        }
    }
}

@Composable
private fun MarketStatisticsRow(marketplaceStatistics: MarketplaceStatistics) {
    with(marketplaceStatistics) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_market_statistics_title),
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                MarketStatisticsCard(
                    iconRes = R.drawable.available_market_items,
                    titleRes = R.string.home_market_statistics_available_items_title,
                    value = countAvailableMarketItems.toString(),
                    backgroundColor = Purple80
                )
                MarketStatisticsCard(
                    iconRes = R.drawable.sold_market_items,
                    titleRes = R.string.home_market_statistics_sold_items_title,
                    value = countSoldMarketItems.toString(),
                    backgroundColor = PurpleGrey80
                )
                MarketStatisticsCard(
                    iconRes = R.drawable.cancelled_market_items,
                    titleRes = R.string.home_market_statistics_cancelled_items_title,
                    value = countCanceledMarketItems.toString(),
                    backgroundColor = Pink80
                )
            }
        }
    }
}

@Composable
private fun MarketStatisticsCard(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    value: String,
    backgroundColor: Color
) {
    Box(modifier = Modifier
        .size(200.dp)
        .padding(horizontal = 8.dp)
        .border(
            2.dp,
            Color.White,
            RoundedCornerShape(30.dp)
        )
        .clip(RoundedCornerShape(30.dp))
        .background(backgroundColor)) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
                .align(Alignment.BottomEnd),
            painter = painterResource(id = iconRes),
            contentDescription = "market_items"
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .align(Alignment.TopCenter)) {
            Text(
                text = stringResource(id = titleRes),
                color = Color.White,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = value,
                color = Color.White,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
private fun MarketplaceRow(
    context: Context,
    @StringRes titleRes: Int,
    items: Iterable<ArtCollectibleForSale>,
    onMarketItemSelected: (tokenId: BigInteger) -> Unit
) {
    if(!Iterables.isEmpty(items)) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Text(
                text = stringResource(id = titleRes),
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontFamily = montserratFontFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleLarge
            )
            ArtCollectibleForSaleList(context, items, onMarketItemSelected)
        }
    }
}

@Composable
private fun ArtCollectibleForSaleList(
    context: Context,
    items: Iterable<ArtCollectibleForSale>,
    onItemSelected: (tokenId: BigInteger) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(Iterables.size(items)) { idx ->
            with(Iterables.get(items, idx)) {
                ArtCollectibleForSaleCard(context = context, artCollectibleForSale = this) {
                    onItemSelected(token.id)
                }
            }
        }
    }
}