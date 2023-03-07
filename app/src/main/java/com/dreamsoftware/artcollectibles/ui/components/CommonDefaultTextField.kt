package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

val CommonDefaultTextFieldModifier = Modifier.padding(vertical = 15.dp).width(300.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDefaultTextField(
    modifier: Modifier = CommonDefaultTextFieldModifier,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    value: String? = null,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    isSingleLine: Boolean = true,
    onValueChanged: (newValue: String) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        modifier = modifier,
        value = value.orEmpty(),
        enabled = isEnabled,
        readOnly = isReadOnly,
        label = { Text(stringResource(id = labelRes), fontFamily = montserratFontFamily) },
        placeholder = { Text(stringResource(id = placeHolderRes), fontFamily = montserratFontFamily) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        singleLine = isSingleLine,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        maxLines = if(isSingleLine) 1 else Int.MAX_VALUE,
        onValueChange = onValueChanged)
}