package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory

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
        onExpandedChange = { expanded = !expanded },
    ) {
        CommonDefaultTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            labelRes = labelRes,
            placeHolderRes = placeHolderRes,
            value = selectedOptionText,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            onValueChanged = { value ->
                categories.find { it.name == value }?.let(onCategorySelected)
                selectedOptionText = value
            }
        )
        // filter options based on text field value
        val filteringCategories = categories.filter {
            it.name.contains(selectedOptionText, ignoreCase = true)
        }
        if (filteringCategories.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                filteringCategories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = {
                            selectedOptionText = selectionOption.name
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}