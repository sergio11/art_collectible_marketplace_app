package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.ui.theme.Purple500

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @StringRes text: Int,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(percent = 50),
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.White,
            shape = RoundedCornerShape(percent = 50)
        ).then(modifier),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple500,
            contentColor = Color.White
        )
    ) {
        Text(
            stringResource(text),
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}