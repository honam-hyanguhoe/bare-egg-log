package com.org.egglog.data.group.di

import com.org.egglog.data.community.usecase.GetHotPostListUseCaseImpl
import com.org.egglog.data.group.usecase.CreateGroupUseCaseImpl
import com.org.egglog.data.group.usecase.GetGroupInfoUseCaseImpl
import com.org.egglog.data.group.usecase.GetGroupListUseCaseImpl
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupModule {
    @Binds
    abstract fun bindGetGroupListUseCaseImpl(uc: GetGroupListUseCaseImpl): GetGroupListUseCase

    @Binds
    abstract fun bindCreateGroupUseCaseImpl(uc : CreateGroupUseCaseImpl) : CreateGroupUseCase

    @Binds
    abstract fun getGroupInfoUseCaseImpl(uc : GetGroupInfoUseCaseImpl) : GetGroupInfoUseCase
}