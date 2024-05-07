package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.community.posteditor.model.Post
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.presentation.data.HotPostInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getHotPostListUseCase: GetHotPostListUseCase
): ViewModel(), ContainerHost<PostListState, PostListSideEffect>{
    override val container: Container<PostListState, PostListSideEffect> = container(
        initialState = PostListState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {postSideEffect(PostListSideEffect.Toast(throwable.message.orEmpty()))}
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        val tokens = getTokenUseCase()
        val hotPostList = getHotPostListUseCase(tokens.first!!).getOrThrow()
    }

    fun onClickSearch() = intent {  }

    fun onClickNotification() = intent { }

    fun onClickPost() = intent {
    }

    fun onClickWriteButton() = intent {
        postSideEffect(PostListSideEffect.NavigateToWriteScreen)
    }

}

class PostListState(
    val hotPostList: List<HotPostInfo> = emptyList()
)

sealed interface PostListSideEffect {
    class Toast(val message: String) : PostListSideEffect

    data object NavigateToWriteScreen : PostListSideEffect
}
