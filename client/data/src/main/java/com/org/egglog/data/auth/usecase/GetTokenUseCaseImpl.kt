package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.data.datastore.UserDataStore
import javax.inject.Inject

class GetTokenUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : GetTokenUseCase {
    override suspend fun invoke(): Pair<String?, String?> {
        return userDataStore.getToken()
    }
}