package com.org.egglog.data.community.usecase

import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.CommunityGroupData
import com.org.egglog.domain.community.usecase.GetCommunityGroupUseCase
import javax.inject.Inject

class GetCommunityGroupUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
):GetCommunityGroupUseCase {
    override suspend fun invoke(accessToken: String): Result<CommunityGroupData> = kotlin.runCatching {
        communityService.getCommunityGroup(accessToken).dataBody!!.toDomainModel()
    }
}