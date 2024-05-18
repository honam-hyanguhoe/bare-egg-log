package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.auth.model.HospitalAuth

interface CertificateBadgeUseCase {
    suspend operator fun invoke(
        accessToken: String,
        nurseCertificationImgUrl: ByteArray,
        hospitalCertificationImgUrl: ByteArray
    ): Result<HospitalAuth?>
}