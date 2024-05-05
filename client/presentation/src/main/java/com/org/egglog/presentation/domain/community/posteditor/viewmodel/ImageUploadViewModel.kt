//package com.org.egglog.presentation.domain.community.posteditor.viewmodel
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.net.Uri
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
//import com.org.egglog.presentation.utils.resizeImage
//import dagger.hilt.android.lifecycle.HiltViewModel
//import org.orbitmvi.orbit.ContainerHost
//import org.orbitmvi.orbit.syntax.simple.intent
//import org.orbitmvi.orbit.syntax.simple.reduce
//import org.orbitmvi.orbit.viewmodel.container
//import javax.inject.Inject
//
//@HiltViewModel
//class ImageUploadViewModel @Inject constructor(
//    private val writePostUseCase: WritePostUseCase,
//) : ViewModel(), ContainerHost<ImageUploadState, Nothing> {
//    override val container = container<ImageUploadState, Nothing>(ImageUploadState())
//
//    fun onUploaderClick() = intent {
//        Log.d("커뮤니티", "이미지 업로더 클릭")
//        //
//    }
//
//    fun handleImageSelection(context: Context, uri: Uri) = intent {
//        Log.d("커뮤니티", "이미지 uri $uri")
//
//        val resizedImage = resizeImage(context, uri, 800, 600)
//        resizedImage?.let {
//            reduce {
//                state.copy(
//                    uploadedImages = state.uploadedImages + resizedImage
//                )
//            }
//            Log.d("커뮤니티", "이미지 업로드 ${state.uploadedImages}")
//        }
//    }
//}
//
