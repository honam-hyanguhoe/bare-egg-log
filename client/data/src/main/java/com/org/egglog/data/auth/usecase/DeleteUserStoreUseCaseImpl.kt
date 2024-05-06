package com.org.egglog.data.auth.usecase

import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import javax.inject.Inject

class DeleteUserStoreUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : DeleteUserStoreUseCase {
    override suspend fun invoke(): Unit {
        return userDataStore.clear()
    }
}