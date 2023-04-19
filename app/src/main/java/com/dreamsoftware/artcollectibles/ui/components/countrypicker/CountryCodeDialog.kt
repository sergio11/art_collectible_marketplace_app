package com.dreamsoftware.artcollectibles.ui.components.countrypicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.*
import kotlin.collections.ArrayList

@Composable
internal fun CountryCodeDialog(
    textSearchedState: MutableState<TextFieldValue>,
    isOpenedState: MutableState<Boolean>,
    pickedCountry: (String) -> Unit
) {
    val countries = getListOfCountries()
    var filteredCountries: List<String>
    if (isOpenedState.value) {
        Dialog(onDismissRequest = { isOpenedState.value = false }) {
            Box(
                Modifier
                    .size(480.dp, 480.dp)
                    .background(Color.White)
            ) {
                LazyColumn(Modifier.padding(8.dp)) {
                    val searchedText = textSearchedState.value.text
                    filteredCountries = if (searchedText.isEmpty()) {
                        countries
                    } else {
                        val resultList = ArrayList<String>()
                        for (country in countries) {
                            if (country.lowercase(Locale.getDefault())
                                    .contains(searchedText.lowercase(Locale.getDefault()))
                            ) {
                                resultList.add(country)
                            }
                        }
                        resultList
                    }
                    item {
                        SearchView(state = textSearchedState)
                    }
                    items(filteredCountries.size) { index ->
                        CountryItem(
                            countryText = filteredCountries[index],
                            onItemClick = { selectedCountry ->
                                isOpenedState.value = false
                                pickedCountry(selectedCountry)
                            }
                        )
                    }
                }
            }

        }
    }
}

fun getListOfCountries(): ArrayList<String> {
    val isoCountryCodes = Locale.getISOCountries()
    val countryListWithEmojis = ArrayList<String>()
    for (countryCode in isoCountryCodes) {
        val locale = Locale("", countryCode)
        val countryName = locale.displayCountry
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
        val flag =
            (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
        countryListWithEmojis.add("$countryName $flag")
    }
    return countryListWithEmojis
}