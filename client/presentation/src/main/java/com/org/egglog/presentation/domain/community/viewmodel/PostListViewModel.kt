package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.posteditor.model.Post
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import com.org.egglog.presentation.data.HotPostInfo
import com.org.egglog.presentation.data.PreviewPostInfo
import com.org.egglog.presentation.data.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getHotPostListUseCase: GetHotPostListUseCase,
    private val getPostListUseCase: GetPostListUseCase
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

        var hotPostList = getHotPostListUseCase("Bearer ${tokens.first}").map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrDefault(listOf())
        val postList = getPostListUseCase("Bearer ${tokens.first!!}", state.hospitalId, state.groupId, state.searchWord, state.lastBoardId).map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrThrow()

        reduce {
            state.copy(
                hotPostList = if(hotPostList.isEmpty()) listOf() else hotPostList,
                postList = if(postList.isEmpty()) listOf() else postList,
            )
        }
    }

    fun onClickSearch() = intent {  }

    fun onClickNotification() = intent { }

    fun onClickPost() = intent {
    }

    fun onClickWriteButton() = intent {
        postSideEffect(PostListSideEffect.NavigateToWriteScreen)
    }

}

data class PostListState(
    val hospitalId: Int? = null,
    val groupId: Int? = null,
    val searchWord: String? = null,
    val lastBoardId: Int? = null,
    val hotPostList: List<HotPostInfo> = listOf(),
    val postList: List<PreviewPostInfo> = listOf()
)

sealed interface PostListSideEffect {
    class Toast(val message: String) : PostListSideEffect

    data object NavigateToWriteScreen : PostListSideEffect
}
