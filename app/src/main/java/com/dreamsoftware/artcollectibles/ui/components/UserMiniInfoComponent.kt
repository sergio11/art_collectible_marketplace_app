package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun UserMiniInfoComponent(modifier: Modifier, userInfo: UserInfo?) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .then(modifier)
    ) {
        UserAccountProfilePicture(size = 50.dp, userInfo = userInfo)
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = userInfo?.name ?: stringResource(id = R.string.no_text_value),
                fontFamily = montserratFontFamily,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            userInfo?.professionalTitle?.let {
                Text(
                    text = it,
                    fontFamily = montserratFontFamily,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}