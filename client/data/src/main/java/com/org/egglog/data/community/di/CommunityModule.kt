package com.org.egglog.data.community.di

import com.org.egglog.data.community.usecase.GetHotPostListUseCaseImpl
import com.org.egglog.data.community.usecase.GetPostListUseCaseImpl
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CommunityModule {

    @Binds
    abstract fun bindGetHotPostListUseCaseImpl(uc: GetHotPostListUseCaseImpl): GetHotPostListUseCase

    @Binds
    abstract fun bindGetPostListUseCaseImpl(uc: GetPostListUseCaseImpl): GetPostListUseCase
}