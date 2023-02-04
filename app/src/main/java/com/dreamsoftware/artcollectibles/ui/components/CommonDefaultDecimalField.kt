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
import com.dreamsoftware.artcollectibles.ui.components.core.CurrencyAmountInputVisualTransformation
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

val CommonDefaultDecimalFieldModifier = Modifier
    .padding(vertical = 20.dp)
    .width(300.dp)

const val DEFAULT_NUMBER_OF_DECIMALS = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDefaultDecimalField(
    modifier: Modifier = CommonDefaultDecimalFieldModifier,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    value: String? = null,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    numberOfDecimals: Int = DEFAULT_NUMBER_OF_DECIMALS,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChanged: (newValue: String) -> Unit = {}
) {
    TextField(
        modifier = modifier,
        value = value.orEmpty(),
        enabled = isEnabled,
        readOnly = isReadOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = { Text(stringResource(id = labelRes), fontFamily = montserratFontFamily) },
        placeholder = {
            Text(
                stringResource(id = placeHolderRes),
                fontFamily = montserratFontFamily
            )
        },
        visualTransformation = CurrencyAmountInputVisualTransformation(
            fixedCursorAtTheEnd = false,
            numberOfDecimals = numberOfDecimals
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        maxLines = 1,
        onValueChange = {
            onValueChanged(
                if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
            )
        }
    )
}