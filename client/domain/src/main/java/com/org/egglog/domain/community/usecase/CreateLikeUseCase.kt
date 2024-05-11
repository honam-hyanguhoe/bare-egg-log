package com.org.egglog.domain.community.usecase

interface CreateLikeUseCase {

    suspend operator fun invoke(accessToken: String, boardId: Int): Result<Boolean>
}