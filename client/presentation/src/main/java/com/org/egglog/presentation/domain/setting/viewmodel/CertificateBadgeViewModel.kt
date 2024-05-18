package com.org.egglog.presentation.domain.setting.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.setting.usecase.CertificateBadgeUseCase
import com.org.egglog.presentation.domain.community.viewmodel.PostSideEffect
import com.org.egglog.presentation.utils.bitmapToByteArray
import com.org.egglog.presentation.utils.resizeImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class CertificateBadgeViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val certificateBadgeUseCase: CertificateBadgeUseCase
): ViewModel() , ContainerHost< CertificateBadgeState,CertificateBadgeSideEffect >{
    override val container: Container<CertificateBadgeState, CertificateBadgeSideEffect> = container(
        initialState = CertificateBadgeState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(CertificateBadgeSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )


    private val _type = MutableStateFlow<String>("nurse")
    val type : StateFlow<String> = _type.asStateFlow()
    private var accessToken: String? = null
    init {
        intent {
            accessToken = getTokenUseCase().first
            setType("nurse")
        }
    }

    fun setType(value: String) {
        _type.value = value
    }

    fun onClickCertification() = intent {
        val result = certificateBadgeUseCase(
            accessToken = "Bearer $accessToken",
            nurseCertificationImgUrl = bitmapToByteArray(state.nurseCertificationImgUrl),
            hospitalCertificationImgUrl = bitmapToByteArray(state.hospitalCertificationImgUrl)
        )

        Log.d("badge", "result $result")
        if (result.isSuccess){
            CertificateBadgeSideEffect.Toast("인증되었습니다")
//            postSideEffect(CertificateBadgeSideEffect.NavigateToSettingScreen)
        }
    }
    fun handleImageSelection(context: Context, uri: Uri , type: String) = intent {

        if(type == "nurse"){
            val resizedImage = resizeImage(context, uri, 800, 600)
            resizedImage?.let {
                reduce {
                    state.copy(
                        uploadImages = state.uploadImages + it,
                        nurseCertificationImgUrl = it
                    )
                }
                Log.d("badge", "nurseCertificationImgUrl ${state.nurseCertificationImgUrl}")
            }
        }else if (type == "hospital"){
            val resizedImage = resizeImage(context, uri, 800, 600)
            resizedImage?.let {
                reduce {
                    state.copy(
                        hospitalCertificationImgUrl = it
                    )
                }
            }
            Log.d("badge", "hospitalCertificationImgUrl ${state.hospitalCertificationImgUrl}")
        }
    }
}

@Immutable
data class CertificateBadgeState(
    val uploadImages: List<Bitmap> = listOf(),
    val nurseCertificationImgUrl: Bitmap? = null,
    val hospitalCertificationImgUrl: Bitmap? = null,
)

sealed class CertificateBadgeSideEffect {
    class Toast(val message: String): CertificateBadgeSideEffect()
    object NavigateToSettingScreen : CertificateBadgeSideEffect()
}