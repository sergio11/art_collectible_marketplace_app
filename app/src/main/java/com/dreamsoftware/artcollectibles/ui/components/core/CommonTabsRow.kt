package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.screens.core.model.TabUi

@Composable
fun <T>CommonTabsRow(
    tabs: List<TabUi<T>>,
    tabSelectedIndex: Int,
    onNewTabSelected: (type: T) -> Unit,
) {
    if (tabs.isNotEmpty()) {
        TabRow(
            selectedTabIndex = tabSelectedIndex,
            containerColor = Color.White.copy(alpha = 0.9f)) {
            tabs.forEach { tab ->
                Tab(
                    selected = tab.isSelected,
                    onClick = { onNewTabSelected(tab.type) },
                    icon = {
                        Image(
                            painter = painterResource(tab.iconRes),
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                )
            }
        }
    }
}