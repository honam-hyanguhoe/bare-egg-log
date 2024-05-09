package com.org.egglog.presentation.domain.group.viewmodel

import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.model.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
) : ViewModel(), ContainerHost<GroupListState, Nothing> {
    override val container: Container<GroupListState, Nothing> = container(
        initialState = GroupListState(),
        buildSettings = {})

    init {
        getGroupList()
    }
    private fun getGroupList() = intent{
        val tokens = getTokenUseCase()
    }
}

data class GroupListState(
    val groupList: List<Group> = listOf(
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3)
    )
)