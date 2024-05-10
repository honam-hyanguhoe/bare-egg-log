package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostListUseCase: GetPostListUseCase,
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel(), ContainerHost<PostSearchState, PostSearchSideEffect> {


    private var groupId: Int? = savedStateHandle.get<Int>("groupId") ?: throw IllegalArgumentException("groupId is required")
    private var hospitalId: Int? = savedStateHandle.get<Int>("hospitalId") ?: throw IllegalArgumentException("hospitalId is required")
    private var accessToken: String? = null

    override val container: Container<PostSearchState, PostSearchSideEffect> = container(
        initialState = PostSearchState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(PostSearchSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        loadAccessToken()
    }

    fun loadAccessToken() = intent {
        accessToken = getTokenUseCase().first
    }

    @OptIn(OrbitExperimental::class)
    fun onSearchChange(value: String) = blockingIntent {
        reduce {
            state.copy(
                searchWord = value
            )
        }
    }

    fun onClickDone() = intent {
        Log.e(
            "PostListViewModel",
            "${hospitalId}, ${groupId}에 ${state.searchWord} 검색할거임"
        )

        if(hospitalId == -1) {
            hospitalId = null
        }
        if(groupId == -1) {
            groupId = null
        }

        Log.e("PostSearchViewModel", "$hospitalId $groupId 에 검색합니다. ${getPostListUseCase(
            "Bearer ${accessToken}",
            hospitalId,
            groupId,
            state.searchWord
        ).getOrThrow()}")
        val searchListFlow = getPostListUseCase(
            "Bearer ${accessToken}",
            hospitalId,
            groupId,
            state.searchWord
        ).getOrThrow()

        reduce {
            state.copy(
                searchListFlow = searchListFlow!!
            )
        }
    }


}

data class PostSearchState(
    val searchWord: String? = null,
    val searchListFlow: Flow<PagingData<PostData>> = emptyFlow()
)

sealed interface PostSearchSideEffect {
    class Toast(val message: String) : PostSearchSideEffect
}