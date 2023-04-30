package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.Purple80

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
            modifier = modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null, // decorative
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(100.dp)
                )
            Column(modifier = Modifier.padding(16.dp)) {
                CommonText(
                    type = CommonTextTypeEnum.LABEL_LARGE,
                    titleRes = titleRes,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
                CommonText(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleRes = descriptionRes,
                    textAlign = TextAlign.Center
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
                        CommonText(
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                            type = CommonTextTypeEnum.LABEL_SMALL,
                            titleText = stringResource(id = it),
                            textColor = Color.Gray
                        )
                    }
                }
                successRes?.let {
                    TextButton(enabled = isAcceptEnabled, onClick = onAcceptClicked) {
                        CommonText(
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                            type = CommonTextTypeEnum.LABEL_SMALL,
                            titleText = stringResource(id = it),
                            textColor = Color.Black
                        )
                    }
                }
            }
        }
    }
}