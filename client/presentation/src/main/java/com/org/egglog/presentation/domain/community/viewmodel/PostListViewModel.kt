package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.posteditor.model.Post
import com.org.egglog.domain.community.usecase.GetCommunityGroupUseCase
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import com.org.egglog.presentation.data.CommunityGroupInfo
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
    private val getPostListUseCase: GetPostListUseCase,
    private val getCommunityGroupUseCase: GetCommunityGroupUseCase
): ViewModel(), ContainerHost<PostListState, PostListSideEffect>{

    private var hotPostList: List<HotPostInfo> = emptyList()
    private var postList: List<PreviewPostInfo> = emptyList()
    private var groupList: CommunityGroupInfo ?= null
    private var accessToken: String ?= null
    private var categoryList =  mutableListOf<Pair<Int, String>>()

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
        accessToken = tokens.first!!

        Log.e("PostListViewModel", "token은 ${accessToken}")
        hotPostList = getHotPostListUseCase("Bearer ${accessToken}").map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrDefault(listOf())
        Log.e("PostLitViewModel", "급상승 리스트는 ${hotPostList}")

        postList = getPostListUseCase("Bearer ${accessToken}", state.hospitalId, state.groupId, state.searchWord, state.lastBoardId).map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrThrow()
        Log.e("PostLitViewModel", "글 리스트는 ${postList}")

        groupList = getCommunityGroupUseCase("Bearer ${accessToken}").map{ it.toUiModel()}.getOrThrow()
        Log.e("PostListViewModel", "그룹리스트는 ${groupList}")

        categoryList.add(0 to "전체")
        if(groupList != null) {
            categoryList.add(groupList!!.hospitalId to groupList!!.hospitalName)
            groupList?.groupList?.forEach { group ->
                categoryList.add(group.groupId to group.groupName)
            }
        }

        Log.e("PostListViewModel", "카테고리 리스트는 ${categoryList}")

        reduce {
            state.copy(
                hotPostList = if(hotPostList.isEmpty()) listOf() else hotPostList,
                postList = if(postList.isEmpty()) listOf() else postList,
                categoryList = if(categoryList.isEmpty()) listOf((0 to "전체")) else categoryList
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

    fun onSelectCategoryIndex(index: Int) = intent {
        // 선택된 index 처리를 여기서 수행
        Log.e("PostListViewModel", "선택된 index는 ${index}이며 Category는 ${categoryList[index]}입니다")
        var groupId: Int ?= null
        var hospitalId: Int ?= null
        if(index == 0) {
            groupId = null
            hospitalId = null
        } else if (index == 1) {
            groupId = null
            hospitalId = categoryList[index].first
        } else {
            groupId = categoryList[index].first
            hospitalId = null
        }

        reduce {
            state.copy (
                groupId = groupId,
                hospitalId = hospitalId,
            )
        }

        postList = getPostListUseCase("Bearer ${accessToken}", hospitalId, groupId, state.searchWord, state.lastBoardId).map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrThrow()

        reduce {
            state.copy (
                postList = if(postList.isEmpty()) listOf() else postList,
            )
        }
    }

}

data class PostListState(
    val hospitalId: Int? = null,
    val groupId: Int? = null,
    val searchWord: String? = null,
    val lastBoardId: Int? = null,
    val hotPostList: List<HotPostInfo> = listOf(),
    val postList: List<PreviewPostInfo> = listOf(),
    val categoryList: List<Pair<Int, String>> = listOf((0 to "전체"))
)

sealed interface PostListSideEffect {
    class Toast(val message: String) : PostListSideEffect

    data object NavigateToWriteScreen : PostListSideEffect
}
