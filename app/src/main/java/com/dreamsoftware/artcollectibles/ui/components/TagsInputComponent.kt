package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun TagsInputComponent(
    modifier: Modifier,
    tagList: List<String>
) {
    Box(
        modifier = modifier
    ) {
        FlowRow {
            repeat(tagList.size) {
                TagInput(text = tagList[it])
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagInput(text: String) {
    InputChip(
        selected = false,
        onClick = {  },
        label = { Text(text) },
        trailingIcon = {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Localized description",
                modifier = Modifier
                    .size(InputChipDefaults.IconSize)
                    .clickable { }
            )
        }
    )
}