package com.dreamsoftware.artcollectibles.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onNavigateAction: () -> Unit
) {
    OnBoardingComponent(modifier) {
        onNavigateAction()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnBoardingComponent(
    modifier: Modifier = Modifier,
    onGetStartedNow: () -> Unit
) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_bg_1),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(top = 80.dp)
                    .fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.onboarding_main_title_text),
                    color = Color.White,
                    fontSize = 37.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.fillMaxSize(0.40f))
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(27.dp),
                    border = BorderStroke(3.dp, Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(27.dp)
                    ) {
                        Text(
                            stringResource(R.string.onboarding_subtitle_text),
                            color = Purple500,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                        Text(
                            stringResource(R.string.onboarding_description_text),
                            color = Purple700,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(bottom = 5.dp))
                        CommonButton(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = R.string.onboarding_start_now_button_text,
                            onClick = onGetStartedNow
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewOnBoardingScreen() {
    ArtCollectibleMarketplaceTheme {
        OnBoardingScreen {
            //Navigate to the next screen
        }
    }
}