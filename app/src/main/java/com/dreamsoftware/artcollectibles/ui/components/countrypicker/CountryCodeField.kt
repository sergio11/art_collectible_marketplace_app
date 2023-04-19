package com.dreamsoftware.artcollectibles.ui.components.countrypicker

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import com.dreamsoftware.artcollectibles.ui.components.CommonDefaultTextField
import com.dreamsoftware.artcollectibles.ui.components.CommonDefaultTextFieldModifier

@Composable
fun CountryCodeField(
    modifier: Modifier = CommonDefaultTextFieldModifier,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    selectedCountry: String? = null,
    pickedCountry: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isOpenState = rememberSaveable { mutableStateOf(false) }
    val textSearchedState = remember { mutableStateOf(TextFieldValue(""))}
    Box {
        CommonDefaultTextField(
            modifier = Modifier.onFocusChanged {
                if(it.isFocused) {
                    isOpenState.value = true
                }
            }.then(modifier),
            isReadOnly = true,
            labelRes = labelRes,
            placeHolderRes = placeHolderRes,
            value = selectedCountry.orEmpty(),
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { isOpenState.value = true },
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        CountryCodeDialog(
            textSearchedState = textSearchedState,
            isOpenedState = isOpenState,
            pickedCountry = {
                focusManager.clearFocus()
                pickedCountry(it)
            }
        )
    }
}