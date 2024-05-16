package com.org.egglog.domain.community.posteditor.usecase

interface ModifyPostUseCase {

    suspend operator fun invoke(accessToken: String, boardId: Int, boardTitle: String, boardContent: String, uploadImages: List<ByteArray>): Result<Boolean>
}