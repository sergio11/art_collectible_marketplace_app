package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dreamsoftware.artcollectibles.R

@Composable
fun MaticIconComponent(size: Dp) {
    Image(
        painter = painterResource(id = R.drawable.matic_icon),
        contentDescription = "Matic Icon",
        modifier = Modifier.size(size)
    )
}