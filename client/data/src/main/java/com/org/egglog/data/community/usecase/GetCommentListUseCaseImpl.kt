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
        Log.e("CommentListUseCaseImpl", "댓글 리스트 조회하러 왔어요")
        Log.e("CommentListUseCaseImpl", "댓글 리스트는 ${communityService.getCommentList(accessToken, postId).dataBody?.map {it!!.toDomainModel()}}")
        communityService.getCommentList(accessToken, postId).dataBody?.map {it!!.toDomainModel()}
    }
}