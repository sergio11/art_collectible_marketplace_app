package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import kotlinx.coroutines.delay

private const val NOTIFY_TERM_CHANGED_DELAY = 2500L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    context: Context,
    term: String? = null,
    onCloseClicked: () -> Unit,
    onTermChanged: (String) -> Unit,
    onClearClicked: () -> Unit
) {
    var newTerm by rememberSaveable { mutableStateOf(term.orEmpty()) }
    if(newTerm.isNotBlank()) {
        LaunchedEffect(newTerm) {
            delay(NOTIFY_TERM_CHANGED_DELAY)
            onTermChanged(newTerm)
        }
    }
    TextField(
        value = newTerm,
        onValueChange = { value ->
            newTerm = value
        },
        placeholder = {
          Text(text = context.getString(R.string.default_query_empty), color = Color.White)
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Image(
                modifier = Modifier
                    .padding(15.dp)
                    .size(35.dp)
                    .clickable {
                        onCloseClicked()
                    },
                painter = painterResource(R.drawable.back_icon),
                contentDescription = "Back Icon",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.White)
            )
        },
        trailingIcon = {
            if (!term.isNullOrBlank()) {
                IconButton(
                    onClick = {
                        newTerm = ""
                        onClearClicked()
                    }
                ) {
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(34.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            containerColor = Purple40,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}


@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    SearchView(
        context = LocalContext.current,
        term = "",
        onCloseClicked = {},
        onTermChanged = {},
        onClearClicked = {}
    )
}