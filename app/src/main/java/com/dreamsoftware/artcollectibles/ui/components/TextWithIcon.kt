package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tintColor: Color = Color.Black,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            tint = tintColor,
            imageVector = icon,
            contentDescription = "Icon",
        )
        CommonText(
            modifier = Modifier.padding(start = 5.dp),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            titleText = text,
            textColor = tintColor
        )
    }
}