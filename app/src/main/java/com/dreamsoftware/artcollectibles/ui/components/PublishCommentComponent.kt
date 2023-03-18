package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun PublishCommentComponent(
    modifier: Modifier = Modifier,
    authUserInfo: UserInfo? = null,
    commentsCount: Long? = null,
    onPublishComment: (comment: String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var placeholderEnabled by remember { mutableStateOf(true) }
    var comment by remember { mutableStateOf("") }
    Column(modifier = modifier)  {
        commentsCount?.let {
            if(it > 0) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = stringResource(id = R.string.token_detail_comments_see_more_text, it),
                    fontFamily = montserratFontFamily,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            UserAccountProfilePicture(size = 50.dp, userInfo = authUserInfo)
            BasicTextField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = 8.dp)
                    .onFocusChanged {
                        placeholderEnabled = !it.isFocused
                    },
                value = comment.ifBlank {
                    if(placeholderEnabled) {
                        stringResource(id = R.string.token_detail_comments_publish_placeholder_text)
                    } else {
                        ""
                    }
                },
                onValueChange = {
                    comment = it
                },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .background(Color.LightGray.copy(alpha = 0.6f), RoundedCornerShape(percent = 30))
                            .padding(16.dp)
                            .focusRequester(focusRequester)
                    ) {
                        innerTextField()
                    }
                }
            )
            CommonButton(
                text = R.string.token_detail_comments_publish_button_text,
                widthDp = 120.dp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Purple40
                ),
                buttonShape = ButtonDefaults.textShape,
                onClick = {
                    if(comment.isNotBlank()) {
                        onPublishComment(comment).also {
                            focusManager.clearFocus()
                            comment = ""
                        }
                    }
                }
            )
        }
    }
}