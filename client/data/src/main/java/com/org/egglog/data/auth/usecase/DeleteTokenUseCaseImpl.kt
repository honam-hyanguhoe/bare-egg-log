package com.org.egglog.data.auth.usecase

import com.org.egglog.data.datastore.TokenDataStore
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import javax.inject.Inject

class DeleteTokenUseCaseImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : DeleteTokenUseCase {
    override suspend fun invoke(): Unit {
        return tokenDataStore.clear()
    }
}