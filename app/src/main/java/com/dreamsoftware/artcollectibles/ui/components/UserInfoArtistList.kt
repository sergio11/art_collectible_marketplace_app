package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

@Composable
fun UserInfoArtistList(
    context: Context,
    @StringRes titleRes: Int,
    userList: Iterable<UserInfo>,
    onUserClicked: (userInfo: UserInfo) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(
            text = stringResource(id = titleRes),
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            fontFamily = montserratFontFamily,
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(Iterables.size(userList)) { idx ->
                with(Iterables.get(userList, idx)) {
                    UserInfoArtistCard(modifier = Modifier.width(150.dp).clickable {
                        onUserClicked(this)
                    }, context = context, this)
                }
            }
        }
    }
}