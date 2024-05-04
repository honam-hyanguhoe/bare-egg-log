package com.org.egglog.data.auth.usecase

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.usecase.GetNaverUseCase
import javax.inject.Inject

class GetNaverUseCaseImpl @Inject constructor(
    private val context: Context
): GetNaverUseCase {

    override suspend fun invoke(): Result<Refresh> {
        return try {
            Result.success(Refresh(refreshToken = "", accessToken = ""))
        } catch(e: ApiException) {
            Result.failure(e)
        }
    }
}