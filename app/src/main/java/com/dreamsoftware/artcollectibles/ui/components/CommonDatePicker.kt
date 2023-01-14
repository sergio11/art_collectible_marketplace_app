package com.dreamsoftware.artcollectibles.ui.components

import android.app.DatePickerDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CommonDatePicker(
    modifier: Modifier,
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    value: String? = null,
    onValueChange: (String) -> Unit = {},
    pattern: String = "yyyy-MM-dd",
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val date = if (!value.isNullOrBlank()) LocalDate.parse(value, formatter) else LocalDate.now()
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            onValueChange(LocalDate.of(year, month + 1, dayOfMonth).toString())
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth,
    )
    CommonDefaultTextField(
        labelRes = labelRes,
        placeHolderRes = placeHolderRes,
        isEnabled = false,
        value = value,
        modifier = Modifier.clickable { dialog.show() }.then(modifier),
    )
}