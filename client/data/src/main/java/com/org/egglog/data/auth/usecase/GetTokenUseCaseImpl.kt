package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.data.datastore.TokenDataStore
import javax.inject.Inject

class GetTokenUseCaseImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : GetTokenUseCase {
    override suspend fun invoke(): Pair<String?, String?> {
        return tokenDataStore.getToken()
    }
}