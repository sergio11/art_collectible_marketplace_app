package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton

@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AndroidView(
        modifier = modifier.fillMaxWidth().height(40.dp),
        factory = ::SignInButton,
        update = { button ->
            button.apply {
                isEnabled = enabled
                setSize(SignInButton.SIZE_STANDARD)
            }
        }
    )
}