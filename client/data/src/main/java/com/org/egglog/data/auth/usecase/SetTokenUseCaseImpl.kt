package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.data.datastore.UserDataStore
import javax.inject.Inject

class SetTokenUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : SetTokenUseCase {
    override suspend fun invoke(accessToken: String, refreshToken: String) {
        userDataStore.setToken(accessToken, refreshToken)
    }
}