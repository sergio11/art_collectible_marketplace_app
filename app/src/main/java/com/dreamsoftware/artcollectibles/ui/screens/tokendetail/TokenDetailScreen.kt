package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.math.BigInteger

private val HEADER_HEIGHT = 250.dp
private val TOOLBAR_HEIGHT = 56.dp
private val PADDING_MEDIUM = 16.dp
private val TITLE_PADDING_START = 16.dp
private val TITLE_PADDING_END = 72.dp
private const val TITLE_FONT_SCALE_START = 1f
private const val TITLE_FONT_SCALE_END = 0.66f
private const val PRICE_NUMBER_OF_DECIMALS = 5

data class TokenDetailScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenDetailScreen(
    navController: NavController,
    args: TokenDetailScreenArgs,
    viewModel: TokenDetailViewModel = hiltViewModel(),
    onTokenBurned: () -> Unit
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
            onBurnTokenCalled = ::burnToken,
            onPutItemForSaleCalled = {},
            onWithDrawFromSaleCalled = ::withDrawFromSale,
            onConfirmBurnTokenCalled = ::onConfirmBurnToken,
            onConfirmWithDrawFromSaleCalled = ::onConfirmWithDrawFromSale,
            onConfirmPutForSaleCalled = ::onConfirmPutForSale
        )
    }
}

@Composable
fun TokenDetailComponent(
    context: Context,
    uiState: TokenDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenCalled: () -> Unit,
    onConfirmWithDrawFromSaleCalled: () -> Unit,
    onConfirmPutForSaleCalled: () -> Unit
) {
    LoadingDialog(isShowingDialog = uiState.isLoading)
    val headerHeightPx = with(density) { HEADER_HEIGHT.toPx() }
    val toolbarHeightPx = with(density) { TOOLBAR_HEIGHT.toPx() }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TokenDetailHeader(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            headerHeightPx = headerHeightPx,
        )
        TokenDetailBody(
            scrollState = scrollState,
            uiState = uiState,
            onBurnTokenCalled = onBurnTokenCalled,
            onWithDrawFromSaleCalled = onWithDrawFromSaleCalled,
            onPutItemForSaleCalled = onPutItemForSaleCalled,
            onConfirmBurnTokenCalled = onConfirmBurnTokenCalled,
            onConfirmWithDrawFromSaleCalled = onConfirmWithDrawFromSaleCalled,
            onConfirmPutForSaleCalled = onConfirmPutForSaleCalled
        )
        TokenDetailToolbar(
            scrollState = scrollState,
            uiState = uiState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
        TokenDetailTitle(
            scrollState = scrollState,
            uiState = uiState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
    }
}

@Composable
private fun TokenDetailHeader(
    context: Context,
    scrollState: ScrollState,
    uiState: TokenDetailUiState,
    headerHeightPx: Float
) {
    with(uiState) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(HEADER_HEIGHT)
            .graphicsLayer {
                translationY = -scrollState.value.toFloat() / 2f // Parallax effect
                alpha = (-1f / headerHeightPx) * scrollState.value + 1
            }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(artCollectible?.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.user_placeholder),
                contentDescription = stringResource(R.string.image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000)),
                            startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                        )
                    )
            )
        }
    }
}

@Composable
private fun TokenDetailBody(
    scrollState: ScrollState,
    uiState: TokenDetailUiState,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenCalled: () -> Unit,
    onConfirmWithDrawFromSaleCalled: () -> Unit,
    onConfirmPutForSaleCalled: () -> Unit
) {
    with(uiState) {
        artCollectible?.let {
            //.......................................................................
            ConfirmBurnTokenDialog(uiState, onBurnTokenCalled)
            ConfirmWithdrawFromSaleDialog(uiState, onWithDrawFromSaleCalled)
            ConfirmPutItemForSaleDialog(uiState, onPutItemForSaleCalled)
            //.......................................................................
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(HEADER_HEIGHT))
                if (isTokenOwner) {
                    CommonButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = if (isTokenAddedForSale) {
                            R.string.token_detail_with_draw_from_sale_button_text
                        } else {
                            R.string.token_detail_put_item_for_sale_button_text
                        },
                        onClick = {
                            if (isTokenAddedForSale) {
                                onConfirmWithDrawFromSaleCalled()
                            } else {
                                onConfirmPutForSaleCalled()
                            }
                        }
                    )
                    CommonButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.token_detail_burn_token_button_text,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = onConfirmBurnTokenCalled
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TokenDetailToolbar(
    scrollState: ScrollState,
    uiState: TokenDetailUiState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    val toolbarBottom = headerHeightPx - toolbarHeightPx
    val showToolbar by remember {
        derivedStateOf {
            scrollState.value >= toolbarBottom
        }
    }
    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(Purple700, Purple500)
                )
            ),
            title = {},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun TokenDetailTitle(
    scrollState: ScrollState,
    uiState: TokenDetailUiState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    with(uiState) {
        var titleHeightPx by remember { mutableStateOf(0f) }
        var titleWidthPx by remember { mutableStateOf(0f) }
        artCollectible?.let {
            Text(
                text = "#${it.id} - ${it.name}",
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .graphicsLayer {
                        val collapseRange: Float = (headerHeightPx - toolbarHeightPx)
                        val collapseFraction: Float =
                            (scrollState.value / collapseRange).coerceIn(0f, 1f)

                        val scaleXY = lerp(
                            TITLE_FONT_SCALE_START.dp,
                            TITLE_FONT_SCALE_END.dp,
                            collapseFraction
                        )

                        val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                        val titleYFirstInterpolatedPoint = lerp(
                            HEADER_HEIGHT - titleHeightPx.toDp() - PADDING_MEDIUM,
                            HEADER_HEIGHT / 2,
                            collapseFraction
                        )

                        val titleXFirstInterpolatedPoint = lerp(
                            TITLE_PADDING_START,
                            (TITLE_PADDING_END - titleExtraStartPadding) * 5 / 4,
                            collapseFraction
                        )

                        val titleYSecondInterpolatedPoint = lerp(
                            HEADER_HEIGHT / 2,
                            TOOLBAR_HEIGHT / 2 - titleHeightPx.toDp() / 2,
                            collapseFraction
                        )

                        val titleXSecondInterpolatedPoint = lerp(
                            (TITLE_PADDING_END - titleExtraStartPadding) * 5 / 4,
                            TITLE_PADDING_END - titleExtraStartPadding,
                            collapseFraction
                        )

                        val titleY = lerp(
                            titleYFirstInterpolatedPoint,
                            titleYSecondInterpolatedPoint,
                            collapseFraction
                        )

                        val titleX = lerp(
                            titleXFirstInterpolatedPoint,
                            titleXSecondInterpolatedPoint,
                            collapseFraction
                        )

                        translationY = titleY.toPx()
                        translationX = titleX.toPx()
                        scaleX = scaleXY.value
                        scaleY = scaleXY.value
                    }
                    .onGloballyPositioned { lc ->
                        titleHeightPx = lc.size.height.toFloat()
                        titleWidthPx = lc.size.width.toFloat()
                    }
            )
        }
    }
}


@Composable
private fun ConfirmWithdrawFromSaleDialog(
    uiState: TokenDetailUiState,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        // Confirm with draw from sale dialog
        CommonDialog(
            isVisible = confirmWithDrawFromSale,
            titleRes = R.string.token_detail_with_draw_from_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_with_draw_from_sale_dialog_description_text,
            acceptRes = R.string.token_detail_with_draw_from_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_with_draw_from_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onWithDrawFromSaleCalled(it.id)
                }
            }
        )
    }
}

@Composable
private fun ConfirmBurnTokenDialog(
    uiState: TokenDetailUiState,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = confirmBurnToken,
            titleRes = R.string.token_detail_burn_token_confirm_title_text,
            descriptionRes = R.string.token_detail_burn_token_confirm_description_text,
            acceptRes = R.string.token_detail_burn_token_confirm_accept_button_text,
            cancelRes = R.string.token_detail_burn_token_confirm_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onBurnTokenCalled(it.id)
                }
            }
        )
    }
}

@Composable
private fun ConfirmPutItemForSaleDialog(
    uiState: TokenDetailUiState,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        var putItemForSalePriceState by rememberSaveable { mutableStateOf("") }
        CommonDialog(
            isVisible = confirmPutForSale,
            titleRes = R.string.token_detail_put_item_for_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_put_item_for_sale_dialog_description_text,
            acceptRes = R.string.token_detail_put_item_for_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_put_item_for_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onPutItemForSaleCalled(it.id)
                }
            }
        ) {
            CommonDefaultDecimalField(
                modifier = Modifier.padding(horizontal = 8.dp),
                labelRes = R.string.token_detail_put_item_for_sale_input_price_label,
                placeHolderRes = R.string.token_detail_put_item_for_sale_input_price_placeholder,
                value = putItemForSalePriceState,
                numberOfDecimals = PRICE_NUMBER_OF_DECIMALS,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.matic_icon),
                        contentDescription = "Matic Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                },
                onValueChanged = {
                    putItemForSalePriceState = it
                }
            )
        }
    }
}
