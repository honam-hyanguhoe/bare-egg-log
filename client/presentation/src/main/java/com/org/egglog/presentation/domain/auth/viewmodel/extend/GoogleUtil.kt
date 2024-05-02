package com.org.egglog.presentation.domain.auth.viewmodel.extend

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.org.egglog.presentation.R
import javax.inject.Inject

class GoogleUtil @Inject constructor() {
    private var googleLoginClient: GoogleSignInClient? = null
    private val REQ_GOOGLE_LOGIN = 1001

    fun loginGoogle(activity: Activity){
        val webClientId = activity.getString(R.string.google_client_id)

        var signInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail().build()

        googleLoginClient = GoogleSignIn.getClient(activity, signInOptions)

        googleLoginClient?.let { client ->

            client.signOut().addOnCompleteListener {
                // 로그아웃 후 진행
                activity.startActivityForResult(
                    client.signInIntent,
                    REQ_GOOGLE_LOGIN
                )
            }
        }
    }


    fun handleGoogleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,){

        if(requestCode == REQ_GOOGLE_LOGIN){
            // 유저 로그인
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
}