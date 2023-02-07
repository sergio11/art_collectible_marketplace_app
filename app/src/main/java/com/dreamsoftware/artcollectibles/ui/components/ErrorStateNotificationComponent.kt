package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun ErrorStateNotificationComponent(
    isVisible: Boolean,
    @DrawableRes imageRes: Int,
    @StringRes titleRes: Int,
    isRetryButtonVisible: Boolean = false,
    onRetryCalled: () -> Unit = {}
) {
    if(isVisible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp),
                painter = painterResource(id = imageRes),
                contentDescription = "Content Description"
            )
            Text(
                text = stringResource(id = titleRes),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            if(isRetryButtonVisible) {
                CommonButton(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(300.dp),
                    text = R.string.retry_button_text,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = Color.White
                    ),
                    onClick = onRetryCalled
                )
            }
        }
    }
}