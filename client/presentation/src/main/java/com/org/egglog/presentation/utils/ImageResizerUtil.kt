package com.org.egglog.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, stream)
    return stream.toByteArray()
}
fun resizeImage(
    context: Context,
    imageUri: Uri,
    requestWidth: Int,
    requestHeight: Int,
): Bitmap? {
    val options = BitmapFactory.Options().also {
        it.inJustDecodeBounds = true
    }
    try {
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri), null, options)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
