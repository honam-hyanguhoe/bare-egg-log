package com.org.egglog.data.setting.usecase

import android.util.Log
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.setting.model.BadgeRequest
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.auth.model.HospitalAuth
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import com.org.egglog.domain.setting.usecase.CertificateBadgeUseCase
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupUseCase
import javax.inject.Inject

class CertificateBadgeUseCaseImpl @Inject constructor(
    private val settingService: SettingService,
    private val imageUploadUseCase: ImageUploadUseCase,

    ) : CertificateBadgeUseCase {
    override suspend fun invoke(
        accessToken: String,
        nurseCertificationImgUrl: ByteArray,
        hospitalCertificationImgUrl: ByteArray
    ): Result<HospitalAuth?> = kotlin.runCatching {

        var imageUrls: List<String>? = emptyList()
        val uploadImages: List<ByteArray> =
            listOf(nurseCertificationImgUrl, hospitalCertificationImgUrl)

        imageUrls = imageUploadUseCase.uploadImage(uploadImages, "board").getOrNull()
        Log.d("badge", "imageUrls $imageUrls")

        val requestParam = BadgeRequest(
            nurseCertificationImgUrl = imageUrls?.get(0) ?: "",
            hospitalCertificationImgUrl = imageUrls?.get(1) ?: ""
        )


        val response = settingService.certificateBadge(accessToken, requestParam.toRequestBody())

        response.dataBody?.toDomainModel() ?: null
    }

}