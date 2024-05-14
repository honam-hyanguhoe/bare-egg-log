package com.org.egglog.domain.community.usecase

interface DeleteCommentUseCase {

    suspend operator fun invoke(accessToken: String, commentId: Long): Result<Boolean>
}