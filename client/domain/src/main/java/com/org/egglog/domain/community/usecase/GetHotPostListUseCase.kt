package com.org.egglog.domain.community.usecase

import com.org.egglog.domain.community.model.HotPostData

interface GetHotPostListUseCase {

    suspend operator fun invoke(accessToken: String): Result<List<HotPostData?>>
}