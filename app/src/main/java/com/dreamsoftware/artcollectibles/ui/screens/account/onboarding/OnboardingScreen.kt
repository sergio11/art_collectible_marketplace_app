package com.dreamsoftware.artcollectibles.ui.screens.account.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }
    OnBoardingComponent(
        modifier = modifier,
        snackBarHostState = snackBarHostState,
        onLoginClicked = onNavigateToLogin,
        onSignUpClicked = onNavigateToSignUp
    )
}


@Composable
internal fun OnBoardingComponent(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    AccountScreen(
        modifier = modifier,
        snackBarHostState = snackBarHostState,
        mainTitleRes = R.string.onboarding_main_title_text,
        screenBackgroundRes = R.drawable.common_background
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
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
        CommonButton(
            modifier = Modifier.padding(bottom = 4.dp),
            text = R.string.onboarding_login_button_text,
            onClick = onLoginClicked
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        CommonButton(
            modifier = Modifier.padding(bottom = 4.dp),
            text = R.string.onboarding_signup_button_text,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple700,
                contentColor = Color.White
            ),
            onClick = onSignUpClicked
        )
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