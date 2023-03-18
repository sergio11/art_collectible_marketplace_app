package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun UserFollowersInfoComponent(
    modifier: Modifier = Modifier,
    smallSize: Boolean = false,
    followersCount: Long,
    followingCount: Long,
    onShowFollowers: () -> Unit = {},
    onShowFollowing: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.clickable {
                if(followersCount > 0) {
                    onShowFollowers()
                }
            },
            text = stringResource(id = R.string.profile_followers_count_text, followersCount),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = montserratFontFamily,
            style = with(MaterialTheme.typography) {
                if(smallSize) {
                    bodySmall
                } else {
                    titleSmall
                }
            }
        )
        Text(
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {
                    if(followingCount > 0) {
                        onShowFollowing()
                    }
                },
            color = Color.Black,
            text = stringResource(id = R.string.profile_following_count_text, followingCount),
            fontWeight = FontWeight.Bold,
            fontFamily = montserratFontFamily,
            style = with(MaterialTheme.typography) {
                if(smallSize) {
                    bodySmall
                } else {
                    titleSmall
                }
            }
        )
    }
}