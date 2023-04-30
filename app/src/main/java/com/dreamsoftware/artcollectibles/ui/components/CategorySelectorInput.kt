package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.ui.components.core.CommonDefaultTextField

val CommonCategorySelectorInputModifier = Modifier.padding(vertical = 15.dp).width(300.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorInput(
    modifier: Modifier = CommonCategorySelectorInputModifier,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    category: ArtCollectibleCategory? = null,
    categories: Iterable<ArtCollectibleCategory> = emptyList(),
    onCategorySelected: (category: ArtCollectibleCategory) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(category?.name.orEmpty()) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        CommonDefaultTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            labelRes = labelRes,
            placeHolderRes = placeHolderRes,
            isReadOnly = true,
            value = selectedOptionText,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            categories.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        selectedOptionText = selectionOption.name
                        expanded = false
                        categories.find { it.name == selectedOptionText }?.let(onCategorySelected)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}