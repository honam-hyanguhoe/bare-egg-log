package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.usecase.GetRefreshUseCase
import com.org.egglog.data.auth.service.AuthService
import javax.inject.Inject

class GetRefreshUseCaseImpl @Inject constructor(
    private val authService: AuthService
): GetRefreshUseCase {
    override suspend fun invoke(refresh: String): Result<Refresh> {
        TODO("Not yet implemented")
    }
}