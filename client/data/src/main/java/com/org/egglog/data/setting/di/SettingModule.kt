package com.org.egglog.data.setting.di

import com.org.egglog.data.setting.usecase.DeleteCalendarGroupUseCaseImpl
import com.org.egglog.data.setting.usecase.GetCalendarGroupListUseCaseImpl
import com.org.egglog.data.setting.usecase.GetCalendarGroupMapStoreUseCaseImpl
import com.org.egglog.data.setting.usecase.PostCalendarGroupUseCaseImpl
import com.org.egglog.data.setting.usecase.PostCalendarSyncUseCaseImpl
import com.org.egglog.data.setting.usecase.SetCalendarGroupMapStoreUseCaseImpl
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupListUseCase
import com.org.egglog.domain.setting.usecase.GetCalendarGroupMapStoreUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarGroupUseCase
import com.org.egglog.domain.setting.usecase.PostCalendarSyncUseCase
import com.org.egglog.domain.setting.usecase.SetCalendarGroupMapStoreUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingModule {
    @Binds
    abstract fun bindGetCalendarGroupListUseCase(uc: GetCalendarGroupListUseCaseImpl): GetCalendarGroupListUseCase

    @Binds
    abstract fun bindGetCalendarGroupMapStoreUseCase(uc: GetCalendarGroupMapStoreUseCaseImpl): GetCalendarGroupMapStoreUseCase

    @Binds
    abstract fun bindSetCalendarGroupMapStoreUseCase(uc: SetCalendarGroupMapStoreUseCaseImpl): SetCalendarGroupMapStoreUseCase

    @Binds
    abstract fun bindDeleteCalendarGroupUseCase(uc: DeleteCalendarGroupUseCaseImpl): DeleteCalendarGroupUseCase

    @Binds
    abstract fun bindPostCalendarSyncUseCase(uc: PostCalendarSyncUseCaseImpl): PostCalendarSyncUseCase

    @Binds
    abstract fun bindPostCalendarGroupUseCase(uc: PostCalendarGroupUseCaseImpl): PostCalendarGroupUseCase
}