package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

@Composable
fun UserInfoArtistList(
    context: Context,
    @StringRes titleRes: Int,
    userList: Iterable<UserInfo>,
    onUserClicked: (userUid: String) -> Unit) {
    CollectionRow(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        titleRes = titleRes,
        items = userList,
        onBuildItem = { modifier, item ->
            UserInfoArtistCard(
                modifier = modifier.width(150.dp),
                context = context,
                item
            )
        },
        onItemSelected = {
            onUserClicked(it.uid)
        }
    )
}