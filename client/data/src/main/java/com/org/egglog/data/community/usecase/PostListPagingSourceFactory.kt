package com.org.egglog.data.community.usecase

import com.org.egglog.data.community.service.CommunityService
import javax.inject.Inject

class PostListPagingSourceFactory @Inject constructor(
    private val communityService: CommunityService
)  {

    fun create(accessToken: String, hospitalId: Int?, groupId: Int?, searchWord: String?): PostListPagingSource {
        return PostListPagingSource(communityService, accessToken, hospitalId, groupId, searchWord)
    }
}