package com.org.egglog.domain.community.usecase

import com.org.egglog.domain.community.model.CommunityGroupData

interface GetCommunityGroupUseCase {

    suspend operator fun invoke(accessToken: String): Result<CommunityGroupData>
}