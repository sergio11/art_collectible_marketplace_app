package com.dreamsoftware.artcollectibles.ui.components

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TagsInputComponent(
    modifier: Modifier,
    tagList: List<String>,
    onAddNewTag: (tag: String) -> Unit
) {
    var tagText by remember { mutableStateOf("") }
    Box(
        modifier = modifier
    ) {
        TagsRow(tagList = tagList) {
            BasicTextField(
                modifier = Modifier.onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KEYCODE_ENTER){
                        onAddNewTag(tagText.trim()).also {
                            tagText = ""
                        }
                        true
                    } else {
                        false
                    }
                },
                value = tagText,
                onValueChange = {
                    tagText = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onAddNewTag(tagText.trim()).also {
                        tagText = ""
                    } }
                )
            )
        }
    }
}


