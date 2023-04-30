package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum

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
            CommonText(
                modifier = Modifier.padding(vertical = 2.dp),
                type = CommonTextTypeEnum.LABEL_LARGE,
                titleText = userInfo?.name,
                singleLine = true
            )
            userInfo?.professionalTitle?.let {
                CommonText(
                    modifier = Modifier.padding(vertical = 2.dp),
                    type = CommonTextTypeEnum.LABEL_MEDIUM,
                    titleText = it,
                    textColor = Color.DarkGray,
                    singleLine = true
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