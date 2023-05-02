package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.core.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun ErrorStateNotificationComponent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    @DrawableRes imageRes: Int,
    title: String,
    isRetryButtonVisible: Boolean = false,
    onRetryCalled: () -> Unit = {}
) {
    if (isVisible) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .then(modifier)) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        Color.White.copy(alpha = 0.6f),
                        RoundedCornerShape(percent = 10)
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp),
                    painter = painterResource(id = imageRes),
                    contentDescription = "Content Description",
                    colorFilter = ColorFilter.tint(Purple40)
                )
                CommonText(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleText = title,
                    textColor = DarkPurple,
                    textAlign = TextAlign.Center
                )
                if (isRetryButtonVisible) {
                    CommonButton(
                        text = R.string.retry_button_text,
                        containerColor = Purple40,
                        onClick = onRetryCalled
                    )
                }
            }
        }
    }
}