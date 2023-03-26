package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.screens.tokendetail.*
import com.dreamsoftware.artcollectibles.ui.theme.*

private val HEADER_HEIGHT = 250.dp
private val TOOLBAR_HEIGHT = 56.dp
private val PADDING_MEDIUM = 16.dp
private val TITLE_PADDING_START = 16.dp
private val TITLE_PADDING_END = 72.dp
private const val TITLE_FONT_SCALE_START = 1f
private const val TITLE_FONT_SCALE_END = 0.66f

@Composable
fun CommonDetailScreen(
    context: Context,
    scrollState: ScrollState,
    density: Density,
    isLoading: Boolean = false,
    imageUrl: String? = null,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    val headerHeightPx = with(density) { HEADER_HEIGHT.toPx() }
    val toolbarHeightPx = with(density) { TOOLBAR_HEIGHT.toPx() }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //....................
        CommonDetailHeader(
            context = context,
            scrollState = scrollState,
            headerHeightPx = headerHeightPx,
            imageUrl = imageUrl
        )
        //....................
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(HEADER_HEIGHT))
            content()
        }
        //....................
        CommonDetailToolbar(
            context = context,
            scrollState = scrollState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx,
            imageUrl = imageUrl
        )
        //....................
        CommonDetailTitle(
            scrollState = scrollState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx,
            title = title
        )
    }
}

@Composable
private fun CommonDetailHeader(
    context: Context,
    scrollState: ScrollState,
    imageUrl: String?,
    headerHeightPx: Float
) {
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
                .data(imageUrl)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommonDetailToolbar(
    context: Context,
    scrollState: ScrollState,
    headerHeightPx: Float,
    imageUrl: String?,
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
                    listOf(Purple80, Purple40)
                )
            ),
            navigationIcon = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )
            },
            title = {},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun CommonDetailTitle(
    scrollState: ScrollState,
    title: String?,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }
    Text(
        text = title.orEmpty(),
        color = Color.White,
        fontFamily = montserratFontFamily,
        style = MaterialTheme.typography.displaySmall,
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