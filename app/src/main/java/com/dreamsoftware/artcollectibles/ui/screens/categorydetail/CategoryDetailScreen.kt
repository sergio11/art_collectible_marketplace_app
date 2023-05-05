package com.dreamsoftware.artcollectibles.ui.screens.categorydetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleForSaleCard
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.core.CommonDetailScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.core.produceUiState
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.common.collect.Iterables
import java.math.BigInteger

data class CategoryDetailScreenArgs(
    val uid: String,
    val viewType: ViewTypeEnum
) {
    enum class ViewTypeEnum {
        AVAILABLE_MARKET_ITEMS,
        ALL_ART_COLLECTIBLES
    }
}

@Composable
fun CategoryDetailScreen(
    args: CategoryDetailScreenArgs,
    viewModel: CategoryDetailViewModel = hiltViewModel(),
    onGoToTokenForSaleDetail: (tokenId: BigInteger) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = CategoryDetailUiState(),
        viewModel = viewModel,
        lifecycle = lifecycle
    )
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    with(viewModel) {
        with(args) {
            LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
                if(viewType == CategoryDetailScreenArgs.ViewTypeEnum.AVAILABLE_MARKET_ITEMS) {
                    loadAvailableMarketItemsByCategory(uid)
                } else {
                    loadArtCollectiblesByCategory(uid)
                }
            }
            CategoryDetailComponent(
                context = context,
                uiState = uiState,
                scrollState = scrollState,
                density = density,
                onGoToTokenForSaleDetail = onGoToTokenForSaleDetail,
                onGoToTokenDetail = onGoToTokenDetail,
                onRetryCalled = {
                    if(viewType == CategoryDetailScreenArgs.ViewTypeEnum.AVAILABLE_MARKET_ITEMS) {
                        loadAvailableMarketItemsByCategory(uid)
                    } else {
                        loadArtCollectiblesByCategory(uid)
                    }
                },
                onBackClicked = onBackClicked
            )
        }
    }
}

@Composable
fun CategoryDetailComponent(
    context: Context,
    uiState: CategoryDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onGoToTokenForSaleDetail: (tokenId: BigInteger) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onRetryCalled: () -> Unit,
    onBackClicked: () -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            onBackClicked = onBackClicked,
            imageUrl = uiState.category?.imageUrl,
            title = uiState.category?.name?.ifBlank {
                stringResource(id = R.string.no_text_value)
            } ?: stringResource(id = R.string.no_text_value)
        ) {
            CommonText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                type = CommonTextTypeEnum.TITLE_MEDIUM,
                titleText = uiState.category?.description?.ifBlank {
                    stringResource(id = R.string.no_text_value)
                } ?: stringResource(id = R.string.no_text_value)
            )
            ErrorStateNotificationComponent(
                isVisible = !isLoading && Iterables.isEmpty(items),
                imageRes = R.drawable.not_data_found,
                title = stringResource(id = R.string.category_detail_no_tokens_found_title),
                isRetryButtonVisible = true,
                onRetryCalled = onRetryCalled
            )
            if(!Iterables.isEmpty(items)) {
                CommonText(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleRes = R.string.category_detail_tokens_title,
                    maxLines = 2
                )
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 4.dp),
                crossAxisAlignment = FlowCrossAxisAlignment.Center,
                mainAxisAlignment = MainAxisAlignment.Center,
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                repeat(Iterables.size(items)) {
                    val item = Iterables.get(items, it)
                    if(item is ArtCollectibleForSale) {
                        ArtCollectibleForSaleCard(
                            context = context,
                            reverseStyle = true,
                            artCollectibleForSale = item
                        ) {
                            onGoToTokenForSaleDetail(item.token.id)
                        }
                    } else if(item is ArtCollectible) {
                        ArtCollectibleMiniCard(
                            context = context,
                            reverseStyle = true,
                            artCollectible = item
                        ) {
                            onGoToTokenDetail(item.id)
                        }
                    }
                }
            }
        }
    }
}