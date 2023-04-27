package com.dreamsoftware.artcollectibles.ui.components.core

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
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleImage
import com.dreamsoftware.artcollectibles.ui.components.TextWithImage
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200

@Composable
fun CommonArtCollectibleMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectible: ArtCollectible,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
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
        ArtCollectibleImage(context, artCollectible)
        Column(
            Modifier.fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            CommonText(
                type = CommonTextTypeEnum.TITLE_SMALL,
                titleText = artCollectible.displayName,
                textColor = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Color.Black
                },
                singleLine = true
            )
            CommonText(
                type = CommonTextTypeEnum.BODY_SMALL,
                titleText = artCollectible.metadata.description,
                textColor = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Color.Black
                },
                maxLines = 2
            )
            TextWithImage(
                modifier = Modifier.padding(top = 10.dp),
                imageRes = R.drawable.token_category_icon,
                text = artCollectible.metadata.category.name,
                tintColor = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Color.Black
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        content()
    }
}