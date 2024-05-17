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
                val timeStamp = System.currentTimeMillis().toString()
                val filePath = "/${type}/$timeStamp.jpg"
                val storageRef = storageInstance.reference.child(filePath)
                val uploadTask = storageRef.putBytes(imageData).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                urls.add(downloadUrl.toString())
            }
            return Result.success(urls)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}