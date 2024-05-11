package com.org.egglog.domain.community.usecase

interface DeleteLikeUseCase {

    suspend operator fun invoke(accessToken: String, boardId: Int): Result<Boolean>
}