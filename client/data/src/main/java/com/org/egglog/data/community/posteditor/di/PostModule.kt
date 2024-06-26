package com.org.egglog.data.community.posteditor.di

import com.org.egglog.data.community.posteditor.usecase.ImageUploadUseCaseImpl
import com.org.egglog.data.community.posteditor.usecase.ModifyPostUseCaseImpl
import com.org.egglog.data.community.posteditor.usecase.WritePostUserCaseImpl
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import com.org.egglog.domain.community.posteditor.usecase.ModifyPostUseCase
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {
    @Binds
    abstract fun bindWritePostUseCase(uc : WritePostUserCaseImpl) : WritePostUseCase

    @Binds
    abstract fun bindImageUploadUseCase(uc : ImageUploadUseCaseImpl) : ImageUploadUseCase

    @Binds
    abstract fun bindModifyPostUseCase(uc: ModifyPostUseCaseImpl): ModifyPostUseCase
}