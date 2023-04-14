package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import java.math.BigInteger

@Composable
fun ArtCollectibleForSaleRow(
    context: Context,
    @StringRes titleRes: Int,
    items: Iterable<ArtCollectibleForSale>,
    reverseStyle: Boolean = false,
    onMarketItemSelected: (tokenId: BigInteger) -> Unit
) {
    CollectionRow(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        titleRes = titleRes,
        reverseStyle = reverseStyle,
        items = items,
        onBuildItem = { modifier, item ->
            ArtCollectibleForSaleCard(
                modifier = modifier,
                context = context,
                reverseStyle = reverseStyle,
                artCollectibleForSale = item)
        },
        onItemSelected = {
            onMarketItemSelected(it.token.id)
        }
    )
}