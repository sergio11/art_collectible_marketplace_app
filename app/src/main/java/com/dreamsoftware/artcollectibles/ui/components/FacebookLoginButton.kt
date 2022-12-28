package com.dreamsoftware.artcollectibles.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.utils.FacebookUtil
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

@Composable
fun FacebookLoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSuccess: (LoginResult) -> Unit,
    onCancel: () -> Unit,
    onError: (FacebookException?) -> Unit,
) {
    val callbackManager = FacebookUtil.callbackManager
    val facebookLoginText = stringResource(R.string.txt_connect_with_facebook)
    AndroidView(
        modifier = modifier.fillMaxWidth().height(50.dp),
        factory = ::LoginButton,
        update = { button ->
            button.apply {
                loginText = facebookLoginText
                isEnabled = enabled
                registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        LoginManager.getInstance().logOut()
                        onSuccess(result)
                        Log.d("FACEBOOK_LOGIN", "Token : ${result.accessToken.token}")
                        Log.d("FACEBOOK_LOGIN", "RecentlyGrantedPermissions : ${result.recentlyGrantedPermissions.joinToString(",")}")
                        Log.d("FACEBOOK_LOGIN", "RecentlyDeniedPermissions : ${result.recentlyDeniedPermissions.joinToString(",")}")
                    }
                    override fun onCancel() {
                        onCancel()
                        Log.d("FACEBOOK_LOGIN", "Login : On Cancel")
                    }
                    override fun onError(error: FacebookException) {
                        onError(error)
                        Log.d("FACEBOOK_LOGIN", "Login : ${error.localizedMessage}")
                    }
                })
            }
        }
    )
}