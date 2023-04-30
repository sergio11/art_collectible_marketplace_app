package com.dreamsoftware.artcollectibles.ui.components.core

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.TagsRow
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

val CommonDefaultTagsInputModifier = Modifier
    .padding(vertical = 15.dp)
    .width(300.dp)

@Composable
fun CommonTagsInputComponent(
    modifier: Modifier = CommonDefaultTagsInputModifier,
    @StringRes titleRes: Int,
    @StringRes placeholderRes: Int,
    tagList: List<String>,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit = {}
) {
    var tagText by remember { mutableStateOf("") }
    var textFieldHasFocus by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(27.dp))
            .then(modifier)
    ) {
        CommonText(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            titleRes = titleRes,
            textColor = Purple40
        )
        TagsRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            tagList = tagList,
            onDeleteClicked = onDeleteTag
        ) {
            BasicTextField(
                modifier = Modifier
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                            onAddNewTag(tagText.trim()).also {
                                tagText = ""
                            }
                            true
                        } else {
                            false
                        }
                    }
                    .onFocusChanged {
                        textFieldHasFocus = it.hasFocus
                    },
                value = if (textFieldHasFocus) {
                    tagText
                } else {
                    tagText.ifBlank { stringResource(id = placeholderRes) }
                },
                onValueChange = {
                    tagText = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddNewTag(tagText.trim()).also {
                            tagText = ""
                        }
                    }
                )
            )
        }
    }
}


