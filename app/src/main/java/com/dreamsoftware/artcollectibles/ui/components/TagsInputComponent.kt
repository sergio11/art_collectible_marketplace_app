package com.dreamsoftware.artcollectibles.ui.components

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

val CommonDefaultTagsInputModifier = Modifier.padding(vertical = 15.dp).width(300.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsInputComponent(
    modifier: Modifier = CommonDefaultTagsInputModifier,
    tagList: List<String>,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit = {}
) {
    var tagText by remember { mutableStateOf("") }
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = TextFieldDefaults.filledShape
        )
    ) {
        TagsRow(tagList = tagList, onDeleteClicked = onDeleteTag) {
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


