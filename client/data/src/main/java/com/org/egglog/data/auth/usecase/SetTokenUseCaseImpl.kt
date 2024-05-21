package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.data.datastore.TokenDataStore
import javax.inject.Inject

class SetTokenUseCaseImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : SetTokenUseCase {
    override suspend fun invoke(accessToken: String, refreshToken: String) {
        tokenDataStore.setToken(accessToken, refreshToken)
    }
}