package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum

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
        CommonText(
            modifier = Modifier.clickable {
                if (followersCount > 0) {
                    onShowFollowers()
                }
            },
            type = if (smallSize) {
                CommonTextTypeEnum.BODY_SMALL
            } else {
                CommonTextTypeEnum.TITLE_SMALL
            },
            titleText = stringResource(id = R.string.profile_followers_count_text, followersCount)
        )
        CommonText(
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {
                    if (followingCount > 0) {
                        onShowFollowing()
                    }
                },
            type = if (smallSize) {
                CommonTextTypeEnum.BODY_SMALL
            } else {
                CommonTextTypeEnum.TITLE_SMALL
            },
            titleText = stringResource(id = R.string.profile_following_count_text, followingCount)
        )
    }
}