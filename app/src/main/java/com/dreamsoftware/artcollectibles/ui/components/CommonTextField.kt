package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    modifier: Modifier,
    value: String? = null,
    @StringRes placeHolderRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: () -> Unit
) {
    TextField(
        value = value.orEmpty(),
        placeholder = { Text(stringResource(id = placeHolderRes)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        maxLines = 1,
        onValueChange = {})
}