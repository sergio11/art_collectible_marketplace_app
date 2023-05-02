package com.dreamsoftware.artcollectibles.ui.screens.home

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.theme.*
import com.google.common.collect.Iterables
import java.math.BigInteger

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit,
    onGoToCategoryDetail: (category: ArtCollectibleCategory) -> Unit,
    onGoToUserDetail: (userUid: String) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onGoToAvailableMarketItems: () -> Unit,
    onGoToSellingMarketItems: () -> Unit,
    onGoToMarketHistory: () -> Unit,
    onGoToMarketStatistics: () -> Unit,
    onGoToNotifications: () -> Unit
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
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadData()
        }
        HomeComponent(
            navController = navController,
            snackBarHostState = snackBarHostState,
            context = context,
            uiState = uiState,
            onGoToMarketItemDetail = onGoToMarketItemDetail,
            onGoToMarketHistoryItemDetail = onGoToMarketHistoryItemDetail,
            onGoToCategoryDetail = onGoToCategoryDetail,
            onGoToUserDetail = onGoToUserDetail,
            onGoToTokenDetail = onGoToTokenDetail,
            onGoToAvailableMarketItems = onGoToAvailableMarketItems,
            onGoToSellingMarketItems = onGoToSellingMarketItems,
            onGoToMarketHistory = onGoToMarketHistory,
            onGoToMarketStatistics = onGoToMarketStatistics,
            onGoToNotifications = onGoToNotifications
        )
    }
}

@Composable
private fun HomeComponent(
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    context: Context,
    uiState: HomeUiState,
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit,
    onGoToCategoryDetail: (category: ArtCollectibleCategory) -> Unit,
    onGoToUserDetail: (userUid: String) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onGoToAvailableMarketItems: () -> Unit,
    onGoToSellingMarketItems: () -> Unit,
    onGoToMarketHistory: () -> Unit,
    onGoToMarketStatistics: () -> Unit,
    onGoToNotifications: () -> Unit
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            titleRes = R.string.home_main_title,
            snackBarHostState = snackBarHostState,
            navController = navController,
            hasBottomBar = true,
            menuActions = listOf(
                TopBarAction(
                    iconRes = R.drawable.statistics_icon,
                    onActionClicked = onGoToMarketStatistics
                ),
                TopBarAction(
                    iconRes = R.drawable.notification_icon,
                    onActionClicked = onGoToNotifications
                )
            ),
            navigationAction = TopBarAction(iconRes = R.drawable.splash_app_icon),
            screenContainerColor = Purple40,
            enableVerticalScroll = true,
            screenContent = {
                marketplaceStatistics?.let {
                    MarketStatisticsRow(
                        marketplaceStatistics = it,
                        onShowAllItems = onGoToMarketStatistics
                    )
                }
                if(!Iterables.isEmpty(categories)) {
                    ArtCollectibleCategoryList(
                        context = context,
                        titleRes = R.string.home_collectibles_categories_title,
                        categories = categories,
                        onCategoryClicked = onGoToCategoryDetail
                    )
                }
                if(!Iterables.isEmpty(mostFollowedUsers)) {
                    UserInfoArtistList(
                        context = context,
                        titleRes = R.string.home_featured_artists_title,
                        userList = mostFollowedUsers,
                        onUserClicked = onGoToUserDetail
                    )
                }
                ArtCollectibleForSaleRow(
                    context = context,
                    titleRes = R.string.home_available_items_for_sale_title,
                    items = availableMarketItems,
                    onShowAllItems = onGoToAvailableMarketItems,
                    onMarketItemSelected = onGoToMarketItemDetail
                )
                ArtCollectiblesRow(
                    context = context,
                    titleRes = R.string.home_art_collectibles_featured_title,
                    items = mostLikedTokens,
                    onItemSelected = {
                        onGoToTokenDetail(it.id)
                    }
                )
                ArtCollectiblesRow(
                    context = context,
                    titleRes = R.string.home_most_visited_art_collectibles_title,
                    items = mostVisitedTokens,
                    onItemSelected = {
                        onGoToTokenDetail(it.id)
                    }
                )
                ArtCollectibleForSaleRow(
                    context = context,
                    titleRes = R.string.home_your_items_for_sale_title,
                    items = sellingMarketItems,
                    onShowAllItems = onGoToSellingMarketItems,
                    onMarketItemSelected = onGoToMarketItemDetail
                )
                LastMarketHistoryRow(
                    context = context,
                    titleRes = R.string.home_last_market_history_title,
                    items = marketHistory,
                    onShowAllItems = onGoToMarketHistory,
                    onMarketItemSelected = onGoToMarketHistoryItemDetail
                )
            }
        )
    }
}

@Composable
private fun MarketStatisticsRow(
    marketplaceStatistics: MarketplaceStatistics,
    onShowAllItems: () -> Unit
) {
    with(marketplaceStatistics) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CommonText(
                    modifier = Modifier.padding(8.dp),
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleRes = R.string.home_market_statistics_title,
                    textColor = Color.White
                )
                Image(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(35.dp)
                        .clickable { onShowAllItems() },
                    painter = painterResource(R.drawable.arrow_right_icon),
                    contentDescription = "onShowAllItems",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
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
    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(horizontal = 8.dp)
            .border(
                2.dp,
                Color.White,
                RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
                .align(Alignment.BottomEnd),
            painter = painterResource(id = iconRes),
            contentDescription = "market_items"
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CommonText(
                type = CommonTextTypeEnum.HEADLINE_MEDIUM,
                titleRes = titleRes,
                textColor = Color.White
            )
            CommonText(
                modifier = Modifier.padding(start = 4.dp),
                type = CommonTextTypeEnum.HEADLINE_LARGE,
                titleText = value,
                textColor = Color.White
            )
        }
    }
}