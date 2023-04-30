package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

val CommonTextFieldPasswordModifier = Modifier.padding(vertical = 20.dp).width(300.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextFieldPassword(
    modifier: Modifier = CommonTextFieldPasswordModifier,
    value: String? = null,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    onValueChanged: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(
        modifier = modifier,
        value = value.orEmpty(),
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            cursorColor = Purple200,
            textColor = Purple40,
            placeholderColor = Purple200,
            unfocusedLabelColor = Purple40,
            focusedLabelColor = Purple200,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        label = { Text(text = stringResource(id = labelRes), fontFamily = montserratFontFamily) },
        placeholder = { Text(text = stringResource(id = placeHolderRes), fontFamily = montserratFontFamily) },
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        shape = RoundedCornerShape(27.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, contentDescription = description, tint = Purple40)
            }
        }
    )
}