package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.service.UserService
import javax.inject.Inject

class HospitalPagingSourceFactory @Inject constructor(
    private val userService: UserService
) {
    fun create(search: String): HospitalPagingSource {
        return HospitalPagingSource(userService, search)
    }
}