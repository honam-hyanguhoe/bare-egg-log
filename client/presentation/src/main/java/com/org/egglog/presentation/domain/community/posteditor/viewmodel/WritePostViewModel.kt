package com.org.egglog.presentation.domain.community.posteditor.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import com.org.egglog.presentation.utils.bitmapToByteArray
import com.org.egglog.presentation.utils.resizeImage
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    private val writePostUseCase: WritePostUseCase
) : ViewModel(), ContainerHost<PostState, Nothing> {
    override val container = container<PostState, Nothing>(PostState())

    fun onTitleChange(title: String) = intent {
        reduce {
            state.copy(title = title)
        }
    }

    fun onContentChange(content: String) = intent {
        reduce {
            state.copy(content = content)
        }
    }

    fun onPostClick() = intent {
        Log.d("커뮤니티", "제목 ${state.title} 내용 ${state.content} 사진 ${state.uploadImages}")
        val byteImages = state.uploadImages.map {
            bitmap ->  bitmapToByteArray(bitmap)
        }

        Log.d("커뮤니티", "onPostClick 제목 ${state.title} 내용 ${state.content} 사진 ${state.uploadImages}")
        val postResult = writePostUseCase(
            boardTitle = state.title,
            boardContent = state.content,
            uploadImages = byteImages
        )
        Log.d("커뮤니티 writePostViewModel", "$postResult")
    }

    fun handleImageSelection(context: Context, uri: Uri) = intent {
        Log.d("커뮤니티", "이미지 uri $uri")
        val resizedImage = resizeImage(context, uri, 800, 600)
        resizedImage?.let {
            reduce {
                state.copy(
                    uploadImages = state.uploadImages + it
                )
            }
            Log.d("커뮤니티", "handleImageSelection ${state.uploadImages} -  ${state.title}")
        }
    }
}

data class PostState(
    val title : String = "",
    val content :  String = "",
    val uploadImages: List<Bitmap> = listOf()
)


//            pictureOne = "",
//            pictureTwo = "",
//            pictureThree = "",
//            pictureFour = ""