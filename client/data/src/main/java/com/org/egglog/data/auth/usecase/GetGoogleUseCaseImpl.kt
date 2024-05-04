package com.org.egglog.data.auth.usecase

import com.google.android.gms.common.api.ApiException
import com.org.egglog.domain.auth.usecase.GetGoogleUseCase
import javax.inject.Inject

class GetGoogleUseCaseImpl @Inject constructor(): GetGoogleUseCase {

    override suspend fun invoke(): Result<String> {
        return try{
            Result.success("hi")
        } catch(e: ApiException) {
            Result.failure(e)
        }
    }
}