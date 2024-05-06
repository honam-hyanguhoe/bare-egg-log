package com.org.egglog.data.community.posteditor.usecase

import android.util.Log
import com.org.egglog.data.community.posteditor.service.FirebaseStorageRepository
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import javax.inject.Inject

class ImageUploadUseCaseImpl @Inject constructor(
    private val firebaseStorageRepository: FirebaseStorageRepository
) : ImageUploadUseCase {

    override suspend fun uploadImage(imageDataList: List<ByteArray>, type: String): Result<List<String>> {
        Log.d("커뮤니티", "firebase imageUpload ${imageDataList[0]}")
        return firebaseStorageRepository.uploadImage(imageDataList, type)
    }
}