package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    @StringRes titleRes: Int,
    centerTitle: Boolean = false,
    menuActions: List<TopBarAction> = emptyList()
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = titleRes),
                modifier = if (centerTitle) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier
                },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        },
        actions = {
            menuActions.map {
                IconButton(onClick = it.onActionClicked) {
                    Image(
                        painter = painterResource(it.iconRes),
                        contentDescription = "Icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(25.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Purple40)
    )
}


data class TopBarAction(
    @DrawableRes val iconRes: Int,
    val onActionClicked: () -> Unit
)