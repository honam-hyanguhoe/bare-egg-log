//package com.org.egglog.domain.auth.viewmodel
//
//import androidx.lifecycle.ViewModel
//import com.org.egglog.auth.usecase.GetRefreshUseCase
//import com.org.egglog.auth.usecase.GetTokenUseCase
//import com.org.egglog.auth.usecase.SetTokenUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import org.orbitmvi.orbit.Container
//import org.orbitmvi.orbit.ContainerHost
//import org.orbitmvi.orbit.syntax.simple.intent
//import javax.annotation.concurrent.Immutable
//import javax.inject.Inject
//
//@HiltViewModel
//class SplashViewModel @Inject constructor(
//    private val getRefreshUseCase: GetRefreshUseCase,
//    private val getTokenUseCase: GetTokenUseCase,
//    private val setTokenUseCase: SetTokenUseCase,
//    override val container: Container<RefreshState, SplashSideEffect>
//): ViewModel(), ContainerHost<RefreshState, SplashSideEffect> {
//    init {
//
//    }
//
//    fun load() = intent {
//        val curToken = getTokenUseCase()
//        if(!curToken.first.isNullOrEmpty()) {
//            val tokens = getRefreshUseCase("").getOrThrow()
//            setTokenUseCase(tokens.accessToken, tokens.refreshToken)
//        }
//    }
//}
//
//
//@Immutable
//data class RefreshState(
//    val id: String = "",
//    val password: String = ""
//)
//
//sealed interface SplashSideEffect {
//    class Toast(val message: String): SplashSideEffect
//}