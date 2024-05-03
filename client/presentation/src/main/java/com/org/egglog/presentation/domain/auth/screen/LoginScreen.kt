package com.org.egglog.presentation.domain.auth.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.AuthButton
import com.org.egglog.presentation.domain.auth.activity.MainActivity
import com.org.egglog.presentation.domain.auth.viewmodel.LoginSideEffect
import com.org.egglog.presentation.domain.auth.viewmodel.LoginViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            LoginSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }
    LoginScreen(viewModel::onKakaoClick)
}

@Composable
fun LoginScreen(
    onKakaoClick: () -> Unit
) {
    val token = stringResource(id = R.string.google_web_client_id)
    val context = LocalContext.current
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
//            user = result.user
                         Log.e("result: ", result.user?.email.toString())
        },
        onAuthError = {
//            user = null
            Log.e("result: ", "bye...")
        }
    )

    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            LocalImageLoader(imageUrl = R.drawable.main_logo, modifier = Modifier.size(200.widthPercent(LocalContext.current).dp))
            Spacer(modifier = Modifier.height(150.heightPercent(LocalContext.current).dp))
            Column(
                modifier = Modifier
                    .background(color = NaturalWhite),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Text(text = "SIGN WITH ___________________", color = Gray700, style = Typography.displayMedium)
                Spacer(modifier = Modifier.height(36.heightPercent(LocalContext.current).dp))
                Row{
                    AuthButton(type = "kakao", onClick = { onKakaoClick() })
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "naver", onClick = {  })
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "google", onClick = {
                        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    })
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    ClientTheme {
        LoginScreen()
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider
                    .getCredential(account.idToken!!, null)
                Log.e("GoogleAuth", "${account.idToken}")
                scope.launch {
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    onAuthComplete(authResult)
                }
            } catch (e: ApiException) {
                Log.e("GoogleAuth", e.toString())
                onAuthError(e)
            }
    }
}