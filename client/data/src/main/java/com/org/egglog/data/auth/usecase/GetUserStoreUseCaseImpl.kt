package com.org.egglog.data.auth.usecase

import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import javax.inject.Inject

class GetUserStoreUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
): GetUserStoreUseCase {
    override suspend fun invoke(): UserDetail? {
        return userDataStore.getUser()
    }
}