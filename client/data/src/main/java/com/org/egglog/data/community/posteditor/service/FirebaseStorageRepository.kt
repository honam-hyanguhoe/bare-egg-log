package com.org.egglog.data.community.posteditor.service

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseStorageRepository : StorageRepository {
    private val storageInstance = FirebaseStorage.getInstance()
    override suspend fun uploadImage(
        imageDataList: List<ByteArray>, type: String
    ): Result<List<String>> {
        val urls = mutableListOf<String>()
        try {
            for (imageData in imageDataList) {
                // 파일 경로 생성 로직. 실제 사용 시에는 더 구체적인 파일명 생성 로직을 적용해야 함
                val timeStamp = System.currentTimeMillis().toString()
                val filePath = "/${type}/$timeStamp.jpg"
                val storageRef = storageInstance.reference.child(filePath)
                val uploadTask = storageRef.putBytes(imageData).await()  // 이미지 업로드
                val downloadUrl = uploadTask.storage.downloadUrl.await()  // 업로드된 이미지의 URL 획득
                urls.add(downloadUrl.toString())  // URL을 리스트에 추가
            }
            return Result.success(urls)  // 모든 이미지의 URL 리스트 반환
        } catch (e: Exception) {
            return Result.failure(e)  // 예외 발생 시 실패 결과 반환
        }
    }


}