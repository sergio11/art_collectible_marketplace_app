package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonDetailScreen
import com.dreamsoftware.artcollectibles.ui.components.TextWithImage
import com.dreamsoftware.artcollectibles.ui.components.UserStatisticsComponent
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

data class ArtistDetailScreenArgs(
    val uid: String
)

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    args: ArtistDetailScreenArgs,
    viewModel: ArtistDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = ArtistDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadDetail(uid = args.uid)
        }
        ArtistDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density
        )
    }
}

@Composable
fun ArtistDetailComponent(
    context: Context,
    uiState: ArtistDetailUiState,
    scrollState: ScrollState,
    density: Density
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = userInfo?.photoUrl,
            title = userInfo?.name?.ifBlank {
                stringResource(id = R.string.search_user_info_name_empty)
            } ?: stringResource(id = R.string.search_user_info_name_empty)
        ) {
            val defaultModifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth()
            userInfo?.professionalTitle?.let {
                Text(
                    modifier = defaultModifier,
                    text = it,
                    fontFamily = montserratFontFamily,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Row(
                modifier = defaultModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                userInfo?.location?.let {
                    TextWithImage(
                        imageRes = R.drawable.user_location_icon,
                        text = it
                    )
                }
                userInfo?.birthdate?.let {
                    TextWithImage(
                        imageRes = R.drawable.user_birthdate_icon,
                        text = it
                    )
                }
            }
            TextWithImage(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                imageRes = R.drawable.user_mail_icon,
                text = userInfo?.contact ?: stringResource(id = R.string.no_text_value)
            )
            Text(
                modifier = defaultModifier,
                text = userInfo?.info?.let {
                    it.ifBlank {
                        stringResource(id = R.string.search_user_info_description_empty)
                    }
                } ?: stringResource(id = R.string.no_text_value),
                fontFamily = montserratFontFamily,
                style = MaterialTheme.typography.bodyLarge
            )
            UserStatisticsComponent(
                modifier = defaultModifier,
                itemSize = 40.dp,
                userInfo = userInfo
            )
        }
    }
}