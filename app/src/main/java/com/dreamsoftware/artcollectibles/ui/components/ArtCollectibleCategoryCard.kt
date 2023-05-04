package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.core.CommonAsyncImage
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun ArtCollectibleCategoryCard(modifier: Modifier = Modifier, context: Context, title: String, imageUrl: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(27.dp))
            .height(200.dp)
            .width(280.dp)
            .then(modifier)
    ) {
        CommonAsyncImage(
            modifier = Modifier
                .border(
                    width = 3.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(27.dp)
                )
                .fillMaxHeight()
                .fillMaxWidth(),
            imageUrl = imageUrl,
            context = context
        )
        CommonText(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(color = Purple40.copy(0.6f), shape = RectangleShape)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            textColor = Color.White,
            titleText = title,
            textBold = true,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
