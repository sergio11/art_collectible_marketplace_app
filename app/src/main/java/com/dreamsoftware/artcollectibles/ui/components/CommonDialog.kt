package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.Purple80
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun CommonDialog(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @StringRes cancelRes: Int? = null,
    @StringRes acceptRes: Int? = null,
    onCancelClicked: () -> Unit = {},
    onAcceptClicked: () -> Unit = {},
    isAcceptEnabled: Boolean = true,
    customContent: @Composable ColumnScope.() -> Unit = {}
) {
    if(isVisible) {
        Dialog(onDismissRequest = onCancelClicked) {
            CommonDialogUI(
                modifier,
                titleRes,
                descriptionRes,
                cancelRes,
                acceptRes,
                onCancelClicked,
                onAcceptClicked,
                isAcceptEnabled,
                customContent
            )
        }
    }
}

@Composable
internal fun CommonDialogUI(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @StringRes cancelRes: Int? = null,
    @StringRes successRes: Int? = null,
    onCancelClicked: () -> Unit,
    onAcceptClicked: () -> Unit,
    isAcceptEnabled: Boolean = true,
    customContent: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier.background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),
                )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = titleRes),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    fontFamily = montserratFontFamily,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = descriptionRes),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            customContent()
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Purple80),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                cancelRes?.let {
                    TextButton(onClick = onCancelClicked) {
                        Text(
                            text = stringResource(id = it),
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontFamily = montserratFontFamily,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
                successRes?.let {
                    TextButton(enabled = isAcceptEnabled, onClick = onAcceptClicked) {
                        Text(
                            text = stringResource(id = it),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            fontFamily = montserratFontFamily,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}