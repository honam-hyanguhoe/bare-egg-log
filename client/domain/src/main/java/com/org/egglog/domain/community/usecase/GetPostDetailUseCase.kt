package com.org.egglog.domain.community.usecase

import com.org.egglog.domain.community.model.PostDetailData

interface GetPostDetailUseCase {

    suspend operator fun invoke(accessToken: String, postId: Int): Result<PostDetailData?>
}