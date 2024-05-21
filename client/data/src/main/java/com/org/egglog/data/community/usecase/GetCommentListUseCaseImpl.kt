package com.org.egglog.data.community.usecase

import android.util.Log
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.CommentData
import com.org.egglog.domain.community.usecase.GetCommentListUseCase
import javax.inject.Inject

class GetCommentListUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): GetCommentListUseCase {
    override suspend fun invoke(accessToken: String, postId: Int): Result<List<CommentData>?> = kotlin.runCatching {
        communityService.getCommentList(accessToken, postId).dataBody?.map {it!!.toDomainModel()}
    }
}