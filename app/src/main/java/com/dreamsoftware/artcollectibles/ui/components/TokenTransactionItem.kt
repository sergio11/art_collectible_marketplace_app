package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun TokenTransactionItem(
    modifier: Modifier = Modifier,
    item: ArtCollectibleForSale
) {
    with(item) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Purple200, RoundedCornerShape(percent = 30))
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(percent = 30))
                .padding(8.dp)
                .then(modifier)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(10.dp),
                    painter = painterResource(id = if(sold) {
                        R.drawable.sold_market_items
                    } else if (canceled) {
                        R.drawable.cancelled_market_items
                    } else {
                        R.drawable.available_market_items
                    }),
                    colorFilter = ColorFilter.tint(if(sold) {
                        Color.Green
                    } else if (canceled) {
                        Color.Red
                    } else {
                        Color.Yellow
                    }),
                    contentDescription = "Token Image"
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.token_detail_last_transactions_cancelled_text, seller.name),
                        fontFamily = montserratFontFamily,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left
                    )
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        ArtCollectiblePrice(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            textColor = Color.Black,
                            iconSize = 20.dp,
                            textSize = 14.sp,
                            price = price
                        )
                        (canceledAt ?: soldAt)?.let {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                                text = it.format(),
                                fontFamily = montserratFontFamily,
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                }
            }
        }
    }
}