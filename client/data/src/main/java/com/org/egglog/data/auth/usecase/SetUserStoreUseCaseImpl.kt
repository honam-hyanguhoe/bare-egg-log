package com.org.egglog.data.auth.usecase

import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import javax.inject.Inject

class SetUserStoreUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : SetUserStoreUseCase {
    override suspend fun invoke(user: UserDetail?) {
        userDataStore.setUser(user)
    }
}