package com.org.egglog.data.auth.di

import com.org.egglog.data.auth.usecase.DeleteTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.DeleteUserStoreUseCaseImpl
import com.org.egglog.data.auth.usecase.GetAllHospitalUseCaseImpl
import com.org.egglog.domain.auth.usecase.PostRefreshUseCase
import com.org.egglog.data.auth.usecase.PostRefreshUseCaseImpl
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.data.auth.usecase.GetTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.GetUserStoreUseCaseImpl
import com.org.egglog.data.auth.usecase.GetUserUseCaseImpl
import com.org.egglog.data.auth.usecase.PostLoginUseCaseImpl
import com.org.egglog.data.auth.usecase.PostLogoutUseCaseImpl
import com.org.egglog.domain.auth.usecase.SetTokenUseCase
import com.org.egglog.data.auth.usecase.SetTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.SetUserStoreUseCaseImpl
import com.org.egglog.data.auth.usecase.UpdateUserDeleteUseCaseImpl
import com.org.egglog.data.auth.usecase.UpdateUserFcmTokenUseCaseImpl
import com.org.egglog.data.auth.usecase.UpdateUserJoinUseCaseImpl
import com.org.egglog.data.auth.usecase.UpdateUserModifyUseCaseImpl
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetAllHospitalUseCase
import com.org.egglog.domain.auth.usecase.PostLoginUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.auth.usecase.PostLogoutUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserDeleteUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserFcmTokenUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserJoinUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserModifyUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindPostLoginUseCase(uc: PostLoginUseCaseImpl): PostLoginUseCase

    @Binds
    abstract fun bindPostRefreshUseCaseImpl(uc: PostRefreshUseCaseImpl): PostRefreshUseCase

    @Binds
    abstract fun bindGetTokenUseCaseImpl(uc: GetTokenUseCaseImpl): GetTokenUseCase

    @Binds
    abstract fun bindSetTokenUseCaseImpl(uc: SetTokenUseCaseImpl): SetTokenUseCase

    @Binds
    abstract fun bindGetUserUseCaseImpl(uc: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    abstract fun bindUpdateUserJoinUseCaseImpl(uc: UpdateUserJoinUseCaseImpl): UpdateUserJoinUseCase

    @Binds
    abstract fun bindGetUserStoreUseCaseImpl(uc: GetUserStoreUseCaseImpl): GetUserStoreUseCase

    @Binds
    abstract fun bindSetUserStoreUseCaseImpl(uc: SetUserStoreUseCaseImpl): SetUserStoreUseCase

    @Binds
    abstract fun bindDeleteUserStoreUseCaseImpl(uc: DeleteUserStoreUseCaseImpl): DeleteUserStoreUseCase

    @Binds
    abstract fun bindDeleteTokenUseCaseImpl(uc: DeleteTokenUseCaseImpl): DeleteTokenUseCase

    @Binds
    abstract fun bindGetAllHospitalUseCaseImpl(uc: GetAllHospitalUseCaseImpl): GetAllHospitalUseCase

    @Binds
    abstract fun bindUpdateUserFcmTokenUseCaseImpl(uc: UpdateUserFcmTokenUseCaseImpl): UpdateUserFcmTokenUseCase

    @Binds
    abstract fun bindPostLogoutUseCaseImpl(uc: PostLogoutUseCaseImpl): PostLogoutUseCase

    @Binds
    abstract fun bindUpdateUserDeleteUseCaseImpl(uc: UpdateUserDeleteUseCaseImpl): UpdateUserDeleteUseCase

    @Binds
    abstract fun bindUpdateUserModifyUseCaseImpl(uc: UpdateUserModifyUseCaseImpl): UpdateUserModifyUseCase
}