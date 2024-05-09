package com.org.egglog.domain.auth.model

data class Refresh (
    val token: Token,
    val userDetail: UserDetail?
)