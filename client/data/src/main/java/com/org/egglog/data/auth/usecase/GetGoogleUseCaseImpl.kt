package com.org.egglog.data.auth.usecase

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.usecase.GetGoogleUseCase
import com.org.egglog.presentation.R
import com.org.egglog.presentation.domain.auth.extend.rememberFirebaseAuthLauncher
import javax.inject.Inject

class GetGoogleUseCaseImpl @Inject constructor(
    private val context: Context
): GetGoogleUseCase {
    override suspend fun invoke(): Result<Refresh> {
        return try {
            Result.success(Refresh(refreshToken = "test", accessToken = "test"))
        } catch(e: ApiException) {
            Result.failure(e)
        }
    }
}