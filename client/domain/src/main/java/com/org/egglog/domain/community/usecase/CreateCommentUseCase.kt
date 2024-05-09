package com.org.egglog.domain.community.usecase

interface CreateCommentUseCase {

    suspend operator fun invoke(accessToken: String, boardId: Int, commentContent: String, parentId: Long, tempNickname: String): Result<Boolean>
}
