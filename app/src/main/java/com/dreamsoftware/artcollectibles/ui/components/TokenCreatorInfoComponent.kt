package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.math.BigInteger

@Composable
fun TokenCreatorInfoComponent(modifier: Modifier, userInfo: UserInfo?) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.token_creator_icon),
            contentDescription = "Token creator Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(20.dp)
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = userInfo?.name ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            style = MaterialTheme.typography.titleMedium
        )
    }
}