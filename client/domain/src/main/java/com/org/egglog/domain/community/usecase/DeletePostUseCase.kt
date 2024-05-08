package com.org.egglog.domain.community.usecase

interface DeletePostUseCase {

    suspend operator fun invoke(accessToken: String, postId: Int): Result<Boolean>
}