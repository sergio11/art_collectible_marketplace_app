package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun UserMiniInfoComponent(
    modifier: Modifier = Modifier,
    showPicture: Boolean = true,
    userInfo: UserInfo?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(showPicture) {
            UserAccountProfilePicture(size = 50.dp, userInfo = userInfo)
        }
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.padding(vertical = 2.dp),
                text = userInfo?.name ?: stringResource(id = R.string.no_text_value),
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            userInfo?.professionalTitle?.let {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp),
                    text = it,
                    fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                )
            }
            UserStatisticsComponent(
                modifier = Modifier.padding(vertical = 2.dp),
                itemSize = 15.dp,
                userInfo = userInfo
            )
        }
    }
}