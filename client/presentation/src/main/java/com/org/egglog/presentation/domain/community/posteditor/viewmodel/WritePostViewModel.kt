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
        reduce { state.copy(isLoading = true) }

        val postResult = writePostUseCase(
            boardTitle = state.title,
            boardContent = state.content,
            uploadImages = state.uploadImages.map{
                    bitmap ->  bitmapToByteArray(bitmap)
            }
        )
        postResult.onSuccess {
            reduce { state.copy(isLoading = false) }  // 로딩 종료
        }.onFailure {
            reduce { state.copy(isLoading = false) }  // 오류 발생 시 로딩 종료
        }
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
    val uploadImages: List<Bitmap> = listOf(),
    val isLoading: Boolean = false,
)


sealed class PostSideEffect {
    object NavigateToNextScreen : PostSideEffect()
}