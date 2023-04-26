package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.Purple500

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Purple500,
        contentColor = Color.White
    ),
    widthDp: Dp = 200.dp,
    heightDp: Dp = 50.dp,
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
                    width = 1.dp,
                    color = Color.White,
                    shape = buttonShape
                )
        ),
        shape = buttonShape,
        colors = colors
    ) {
        CommonText(
            modifier = Modifier.padding(vertical = 4.dp),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            titleText = stringResource(text),
            textColor = Color.White,
            textAlign = TextAlign.Center
        )
    }
}