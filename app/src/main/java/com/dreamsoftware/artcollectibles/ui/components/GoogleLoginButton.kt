package com.dreamsoftware.artcollectibles.ui.components

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.extensions.findActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onAuthSuccess: (idToken: String) -> Unit,
    onAuthFailed: () -> Unit
) {
    val context = LocalContext.current
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context.findActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_client_id))
                .requestEmail()
                .build())
    }
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    try {
                        val account = GoogleSignIn.getSignedInAccountFromIntent(it)
                            .getResult(ApiException::class.java)
                        account?.idToken?.let { token -> onAuthSuccess(token) } ?: onAuthFailed()
                        googleSignInClient.signOut()
                    } catch (e: ApiException) {
                        Log.d("GOOGLE_LOGIN", "signInResult:failed code=${e.statusCode}")
                        onAuthFailed()
                    }
                }
            }
        }
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        factory = ::SignInButton,
        update = { button ->
            button.apply {
                isEnabled = enabled
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener {
                    startForResult.launch(googleSignInClient.signInIntent)
                }
            }
        }
    )
}