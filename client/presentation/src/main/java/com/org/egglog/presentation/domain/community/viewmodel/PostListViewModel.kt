package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetCommunityGroupUseCase
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import com.org.egglog.presentation.data.CommunityGroupInfo
import com.org.egglog.presentation.data.HotPostInfo
import com.org.egglog.presentation.data.toUiModel
import com.org.egglog.presentation.domain.setting.viewmodel.SettingSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
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
class PostListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getHotPostListUseCase: GetHotPostListUseCase,
    private val getPostListUseCase: GetPostListUseCase,
    private val getCommunityGroupUseCase: GetCommunityGroupUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel(), ContainerHost<PostListState, PostListSideEffect>{

    private var hotPostList: List<HotPostInfo> = emptyList()
    private var groupList: CommunityGroupInfo ?= null
    private var accessToken: String ?= ""
    private var categoryList =  mutableListOf<Pair<Int, String>>()
    private var groupId: Int? = null
    private var hospitalId: Int? = null

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

        val userInfo = getUserUseCase("Bearer $accessToken").getOrThrow()

        Log.e("PostListViewModel", "token은 ${accessToken}")
        hotPostList = getHotPostListUseCase("Bearer ${accessToken}").map {
            it.map {
                it!!.toUiModel()
            }
        }.getOrDefault(listOf())

        val postListFlow = getPostListUseCase("Bearer ${accessToken}", state.hospitalId, state.groupId, state.searchWord
        ).getOrThrow()

        groupList = getCommunityGroupUseCase("Bearer ${accessToken}").map{ it.toUiModel()}.getOrThrow()

        categoryList.add(0 to "전체")
        if(groupList != null) {
            categoryList.add(groupList!!.hospitalId to groupList!!.hospitalName)
            groupList?.groupList?.forEach { group ->
                categoryList.add(group.groupId to group.groupName)
            }
        }


        reduce {
            state.copy(
                isHospitalAuth =  userInfo?.hospitalAuth != null,
                hotPostList = if(hotPostList.isEmpty()) listOf() else hotPostList,
                postListFlow = postListFlow!!,
                categoryList = if(categoryList.isEmpty()) listOf((0 to "전체")) else categoryList
            )
        }
    }

    fun onSelectCategoryIndex(index: Int) = intent {

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
                categoryName = categoryList[index].second,
                groupId = groupId,
                hospitalId = hospitalId,
            )
        }

        val postListFlow = getPostListUseCase("Bearer ${accessToken}", state.hospitalId, state.groupId, state.searchWord).getOrThrow()

        reduce {
            state.copy (
                postListFlow = postListFlow!!
            )
        }
    }

    fun onSelectedIdx(selectedIdx: Int) = intent {
        when(selectedIdx) {
            0 -> postSideEffect(PostListSideEffect.Toast("출시 준비 중입니다.")) // 일정 페이지로 이동
            1 -> postSideEffect(PostListSideEffect.Toast("출시 준비 중입니다.")) // 그룹 페이지로 이동
            2 -> postSideEffect(PostListSideEffect.NavigateToMainActivity) // 메인 페이지로 이동
            4 -> postSideEffect(PostListSideEffect.NavigateToSettingActivity) // 설정 페이지로 이동
        }
    }

}

data class PostListState(
    val categoryName: String = "전체",
    val hospitalId: Int? = null,
    val groupId: Int? = null,
    val searchWord: String? = null,
    val hotPostList: List<HotPostInfo> = listOf(),
    val postListFlow: Flow<PagingData<PostData>> = emptyFlow(),
    val categoryList: List<Pair<Int, String>> = listOf((0 to "전체")),
    val isHospitalAuth: Boolean = false,
    val selectedIdx: Int = 3,
)

sealed interface PostListSideEffect {
    class Toast(val message: String) : PostListSideEffect

    data object NavigateToMainActivity: PostListSideEffect

    data object NavigateToGroupActivity: PostListSideEffect

    data object NavigateToSettingActivity: PostListSideEffect



}
