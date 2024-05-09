package com.org.egglog.domain.community.usecase

import com.org.egglog.domain.community.model.CommentData

interface GetCommentListUseCase {

    suspend operator fun invoke(accessToken: String, postId: Int): Result<List<CommentData>?>
}