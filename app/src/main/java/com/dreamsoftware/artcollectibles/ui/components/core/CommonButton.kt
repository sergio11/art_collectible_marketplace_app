package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple500

private val DEFAULT_BUTTON_WIDTH = 200.dp
private val DEFAULT_BUTTON_HEIGHT = 55.dp
private val DEFAULT_BUTTON_MODIFIER = Modifier
    .padding(vertical = 8.dp)
    .fillMaxWidth(fraction = 0.80f)
private val DEFAULT_BUTTON_CONTAINER_COLOR = Purple500
private val DEFAULT_BUTTON_CONTENT_COLOR = Color.White

@Composable
fun CommonButton(
    modifier: Modifier = DEFAULT_BUTTON_MODIFIER,
    enabled: Boolean = true,
    containerColor: Color = DEFAULT_BUTTON_CONTAINER_COLOR,
    contentColor: Color = DEFAULT_BUTTON_CONTENT_COLOR,
    widthDp: Dp = DEFAULT_BUTTON_WIDTH,
    heightDp: Dp = DEFAULT_BUTTON_HEIGHT,
    enableBorder: Boolean = true,
    textType: CommonTextTypeEnum = CommonTextTypeEnum.TITLE_MEDIUM,
    buttonShape: Shape = RoundedCornerShape(percent = 50),
    @StringRes text: Int,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(
            Modifier
                .width(widthDp)
                .height(heightDp)
                .clip(buttonShape)
                .border(
                    width = if (enableBorder) {
                        2.dp
                    } else {
                        0.dp
                    },
                    color = containerColor,
                    shape = buttonShape
                )
        ),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enableBorder) {
                containerColor.copy(alpha = 0.7f)
            } else {
                containerColor
            },
            contentColor = contentColor
        )
    ) {
        CommonText(
            modifier = Modifier.padding(vertical = 4.dp),
            type = textType,
            titleText = stringResource(text),
            textColor = contentColor,
            textAlign = TextAlign.Center
        )
    }
}