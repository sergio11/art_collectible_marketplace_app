package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200

@Composable
fun MarketHistoryMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectibleForSale: ArtCollectibleForSale,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit = {}
) {
    with(artCollectibleForSale) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .width(190.dp)
                .border(
                    2.dp, if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Purple200
                    }, RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp))
                .background(
                    color = if (reverseStyle) {
                        Purple200
                    } else {
                        BackgroundWhite
                    }
                )
                .clickable { onClicked() }
                .then(modifier)
        ) {
            MarketItemImage(context, artCollectibleForSale)
            Column(
                Modifier.padding(horizontal = 10.dp)
            ) {
                CommonText(
                    type = CommonTextTypeEnum.TITLE_SMALL,
                    titleText = token.displayName,
                    textColor = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    },
                    singleLine = true
                )
                CommonText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleText = if (sold) {
                        stringResource(
                            id = R.string.token_detail_last_transactions_sold_text,
                            owner?.name.orEmpty(),
                            seller.name
                        )
                    } else if (canceled) {
                        stringResource(
                            id = R.string.token_detail_last_transactions_cancelled_text,
                            seller.name
                        )
                    } else {
                        stringResource(
                            id = R.string.token_detail_last_transactions_put_for_sale_text,
                            seller.name
                        )
                    },
                    maxLines = 3
                )
                ArtCollectiblePrice(
                    modifier = Modifier,
                    textColor = Color.Black,
                    iconSize = 20.dp,
                    textSize = 14.sp,
                    priceData = price
                )
            }
        }
    }
}