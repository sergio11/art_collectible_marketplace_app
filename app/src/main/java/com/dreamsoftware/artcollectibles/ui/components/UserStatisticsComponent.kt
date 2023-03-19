package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun UserStatisticsComponent(
    modifier: Modifier = Modifier,
    itemSize: Dp,
    userInfo: UserInfo?,
    palette: Palette? = null) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatisticItem(itemSize, icon = R.drawable.tokens_bought_count, value = userInfo?.tokensBoughtCount, palette = palette)
        StatisticItem(itemSize, icon = R.drawable.tokens_sold_count, value = userInfo?.tokensSoldCount, palette = palette)
        StatisticItem(itemSize, icon = R.drawable.token_creator_icon_metric, value = userInfo?.tokensCreatedCount, palette = palette)
        StatisticItem(itemSize, icon = R.drawable.tokens_owned_count, value = userInfo?.tokensOwnedCount, palette = palette)
    }
}

@Composable
private fun StatisticItem(
    itemSize: Dp,
    @DrawableRes icon: Int,
    value: Long?,
    palette: Palette? = null
) {
    Row(
        modifier = Modifier.padding(end = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "Statistic Item Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(itemSize)
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = value?.toString() ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            style = MaterialTheme.typography.titleMedium,
            color = palette?.lightVibrantSwatch?.rgb?.let { paletteColor ->
                Color(paletteColor)
            } ?: Color.Black
        )
    }
}