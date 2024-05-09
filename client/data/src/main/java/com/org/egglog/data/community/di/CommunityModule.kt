package com.org.egglog.data.community.di

import com.org.egglog.data.community.usecase.CreateCommentUseCaseImpl
import com.org.egglog.data.community.usecase.CreateLikeUseCaseImpl
import com.org.egglog.data.community.usecase.DeleteCommentUseCaseImpl
import com.org.egglog.data.community.usecase.DeleteLikeUseCaseImpl
import com.org.egglog.data.community.usecase.DeletePostUseCaseImpl
import com.org.egglog.data.community.usecase.GetCommentListUseCaseImpl
import com.org.egglog.data.community.usecase.GetCommunityGroupUseCaseImpl
import com.org.egglog.data.community.usecase.GetHotPostListUseCaseImpl
import com.org.egglog.data.community.usecase.GetPostDetailUseCaseImpl
import com.org.egglog.data.community.usecase.GetPostListUseCaseImpl
import com.org.egglog.domain.community.usecase.CreateCommentUseCase
import com.org.egglog.domain.community.usecase.CreateLikeUseCase
import com.org.egglog.domain.community.usecase.DeleteCommentUseCase
import com.org.egglog.domain.community.usecase.DeleteLikeUseCase
import com.org.egglog.domain.community.usecase.DeletePostUseCase
import com.org.egglog.domain.community.usecase.GetCommentListUseCase
import com.org.egglog.domain.community.usecase.GetCommunityGroupUseCase
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

    @Binds
    abstract fun bindGetCommunityGroupUseCaseImpl(uc: GetCommunityGroupUseCaseImpl): GetCommunityGroupUseCase

    @Binds
    abstract fun bindGetCommentListUseCaseImpl(uc: GetCommentListUseCaseImpl): GetCommentListUseCase

    @Binds
    abstract fun bindDeleteCommentUseCaseImpl(uc: DeleteCommentUseCaseImpl): DeleteCommentUseCase

    @Binds
    abstract fun bindCreateCommentUseCaseImpl(uc: CreateCommentUseCaseImpl): CreateCommentUseCase

    @Binds
    abstract fun bindCreateLikeUseCaseImpl(uc: CreateLikeUseCaseImpl): CreateLikeUseCase

    @Binds
    abstract fun bindDeleteLikeUseCaseImpl(uc: DeleteLikeUseCaseImpl): DeleteLikeUseCase
}