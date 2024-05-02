package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.KakaoOauthToken

interface GetKakaoUseCase {
    suspend operator fun invoke(): Result<KakaoOauthToken>
}