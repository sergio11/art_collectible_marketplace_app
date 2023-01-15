package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun BottomSheetLayout(
    modifier: Modifier,
    onBottomSheetClosed: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    if (!transitionState.targetState &&
        !transitionState.currentState
    ) {
        onBottomSheetClosed()
        return
    }
    AnimatedVisibility(
        modifier = modifier,
        visibleState = transitionState,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 500)
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(Modifier.clickable {
                    transitionState.apply { targetState = false }
                }),
            elevation = CardDefaults.cardElevation(20.dp),
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(27.dp),
            border = BorderStroke(2.dp, Purple700),
            content = content
        )
    }
}

