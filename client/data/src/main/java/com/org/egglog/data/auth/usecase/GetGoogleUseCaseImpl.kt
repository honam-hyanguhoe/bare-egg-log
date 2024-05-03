package com.org.egglog.data.auth.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.org.egglog.domain.auth.usecase.GetGoogleUseCase
import com.org.egglog.presentation.R
import javax.inject.Inject

class GetGoogleUseCaseImpl @Inject constructor(
    private val context: Context,

): GetGoogleUseCase {
    private var googleLoginClient: GoogleSignInClient? = null
    private val REQ_GOOGLE_LOGIN = 1001

    private fun login() {
        val webClientId = context.getString(R.string.google_client_id)
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleLoginClient = GoogleSignIn.getClient(context, signInOptions)
        googleLoginClient?.let { client ->
            client.signOut().addOnCompleteListener {
                val signInIntent = client.signInIntent
                signInIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(signInIntent)
            }
        }
    }

    fun handleGoogleActivityResult(
        requestCode: Int,
        data: Intent?
    ){
        if(requestCode == REQ_GOOGLE_LOGIN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null && account.id != null && account.email != null) {
                    Log.e("test: ", "email =${account.email}, id=${account.id}, token =${account.idToken}")
                }

            } catch (e: ApiException) {
                Log.e("test: ", "google login fail = ${e.message}")
                if (e.statusCode == 12501) {
                    // 사용자 취소
                    Log.e("test: ", "사용자 취소")
                }
            }
        }
    }

    override suspend fun invoke(): Result<String> {
        login()
        return try{
            Result.success("hi")
        } catch(e: ApiException) {
            Result.failure(e)
        }
    }
}