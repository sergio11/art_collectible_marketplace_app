package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.core.ExpandableText
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import java.math.BigInteger

@Composable
fun ArtCollectibleMiniInfoComponent(
    modifier: Modifier = Modifier,
    showPreviewDescription: Boolean = true,
    artCollectible: ArtCollectible?,
    onSeeAllComments: (tokenId: BigInteger) -> Unit = {},
    onSeeLikesByToken: (tokenId: BigInteger) -> Unit = {},
    onSeeVisitorsByToken: (tokenId: BigInteger) -> Unit = {},
    onSeeCreatorDetail: (userInfo: UserInfo) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = artCollectible?.metadata?.name ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artCollectible?.metadata?.createdAt?.format()?.let {
                stringResource(id = R.string.token_detail_created_at_label, it)
            } ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            color = Color.Black,
            style = MaterialTheme.typography.titleSmall
        )
        Row {
            TextWithImage(
                modifier = Modifier.padding(8.dp).clickable {
                    artCollectible?.author?.let(onSeeCreatorDetail)
                },
                imageRes = R.drawable.token_creator_icon,
                text = artCollectible?.author?.name ?: stringResource(id = R.string.no_text_value)
            )
            TextWithImage(
                modifier = Modifier.padding(8.dp),
                imageRes = R.drawable.token_royalty_icon,
                text = artCollectible?.royalty?.let { "$it%" } ?: stringResource(id = R.string.no_text_value)
            )
            FavoriteCountComponent(
                modifier = Modifier.padding(8.dp).clickable {
                    artCollectible?.let {
                        onSeeLikesByToken(it.id)
                    }
                },
                artCollectible = artCollectible
            )
            TextWithIcon(
                modifier = Modifier.padding(8.dp).clickable {
                    artCollectible?.let {
                        onSeeVisitorsByToken(it.id)
                    }
                },
                icon = if ((artCollectible?.visitorsCount ?: 0) > 0) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                text = artCollectible?.visitorsCount?.toString()
                    ?: stringResource(id = R.string.no_text_value_small)
            )
        }
        Row {
            TextWithImage(
                modifier = Modifier.padding(8.dp),
                imageRes = R.drawable.token_category_icon,
                text = artCollectible?.metadata?.category?.name ?: stringResource(id = R.string.no_text_value)
            )
            TextWithImage(
                modifier = Modifier.padding(8.dp).clickable {
                    artCollectible?.let {
                        if(it.commentsCount > 0) {
                            onSeeAllComments(it.id)
                        }
                    }
                },
                imageRes = R.drawable.comments_icon,
                text = artCollectible?.commentsCount?.toString()
                    ?: stringResource(id = R.string.no_text_value_small)
            )
        }
        if(showPreviewDescription) {
            ExpandableText(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = artCollectible?.metadata?.description ?: stringResource(id = R.string.no_text_value),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = montserratFontFamily
            )
        }
    }
}