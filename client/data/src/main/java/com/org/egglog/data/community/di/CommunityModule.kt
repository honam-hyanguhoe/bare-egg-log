package com.org.egglog.data.community.di

import com.org.egglog.data.community.usecase.DeletePostUseCaseImpl
import com.org.egglog.data.community.usecase.GetHotPostListUseCaseImpl
import com.org.egglog.data.community.usecase.GetPostDetailUseCaseImpl
import com.org.egglog.data.community.usecase.GetPostListUseCaseImpl
import com.org.egglog.domain.community.usecase.DeletePostUseCase
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.community.usecase.GetPostDetailUseCase
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

    @Binds
    abstract fun bindGetPostDetailUseCaseImpl(uc: GetPostDetailUseCaseImpl): GetPostDetailUseCase

    @Binds
    abstract fun bindDeletePostUseCaseImpl(uc: DeletePostUseCaseImpl): DeletePostUseCase
}