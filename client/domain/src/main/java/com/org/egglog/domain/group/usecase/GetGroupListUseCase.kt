package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.Group

interface GetGroupListUseCase {
    suspend operator fun invoke(accessToken: String) : Result<List<Group>?>
}