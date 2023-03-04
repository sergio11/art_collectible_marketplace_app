package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun TagsRow(
    modifier: Modifier = Modifier,
    tagList: List<String>?,
    isReadOnly: Boolean = false,
    onDeleteClicked: (tag: String) -> Unit = {},
    content: @Composable () -> Unit = {}) {
    FlowRow(
        modifier = modifier,
        crossAxisAlignment = FlowCrossAxisAlignment.Center
    ) {
        tagList?.let { tags ->
            repeat(tags.size) {
                TagInput(text = tags[it], isReadOnly) {
                    onDeleteClicked(tags[it])
                }
            }
        }
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagInput(
    text: String,
    isReadOnly: Boolean = false,
    onDeleteClicked: () -> Unit = {}
) {
    InputChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = false,
        onClick = {},
        colors = InputChipDefaults.inputChipColors(containerColor = Purple200),
        label = { Text(text.trim(), fontFamily = montserratFontFamily,
            textAlign = TextAlign.Center) },
        trailingIcon = {
            if(!isReadOnly) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Localized description",
                    modifier = Modifier
                        .size(InputChipDefaults.IconSize)
                        .clickable { onDeleteClicked() }
                )
            }
        }
    )
}