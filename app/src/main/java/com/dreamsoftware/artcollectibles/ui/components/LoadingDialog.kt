package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.core.CommonDialog
import com.dreamsoftware.artcollectibles.ui.theme.Purple500

@Composable
fun LoadingDialog(isShowingDialog: Boolean, dismissOnBackPress: Boolean = false, dismissOnClickOutside: Boolean = false) {
    CommonDialog(
        isVisible = isShowingDialog,
        titleRes = R.string.loading_dialog_title_text,
        descriptionRes = R.string.loading_dialog_description_text
    ) {
        DialogContent()
    }
}

@Composable
private fun DialogContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(
                    Alignment.Center
                ),
            color = Purple500
        )
    }
}