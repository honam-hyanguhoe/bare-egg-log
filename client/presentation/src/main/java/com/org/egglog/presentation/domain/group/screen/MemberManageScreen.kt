package com.org.egglog.presentation.domain.group.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.client.data.Profile
import com.org.egglog.domain.group.model.GroupMember
import com.org.egglog.presentation.component.atoms.profileItem.ProfileItem
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.headers.SearchHeader
import com.org.egglog.presentation.component.molecules.swiper.Swiper
import com.org.egglog.presentation.domain.group.navigation.GroupRoute
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailViewModel
import com.org.egglog.presentation.domain.group.viewmodel.MemberManageViewModel
import com.org.egglog.presentation.theme.Indigo500
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MemberManageScreen(
    groupDetailViewModel: GroupDetailViewModel = hiltViewModel(),
    groupId: Long,
    onNavigateToGroupDetailScreen: (groupId: Long) -> Unit,
) {
    val context = LocalContext.current
    val groupDetailState = groupDetailViewModel.collectAsState().value
    groupDetailViewModel.collectSideEffect {
            sideEffect ->
        when(sideEffect){
            is GroupDetailSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    onNavigateToGroupDetailScreen
    MemberManageScreen(
        groupId = groupId,
        onDelete = groupDetailViewModel::onDelete,
        onChangeLeader = groupDetailViewModel::onChangeLeader,
        onClickBack = { groupId: Long -> onNavigateToGroupDetailScreen(groupId) },
        members = groupDetailState.groupInfo.groupMembers ?: emptyList<GroupMember>()
    )
}

@Composable
private fun MemberManageScreen(
    groupId: Long,
    onDelete: (Long) -> Unit,
    onChangeLeader: (GroupMember) -> Unit,
    onClickBack: (groupId: Long) -> Unit,
    members: List<GroupMember> = emptyList()
) {
    val context = LocalContext.current

    Log.d("memberManageScreen", "members $members")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .width(340.widthPercent(context).dp)
                .background(NaturalWhite)
        ) {
            BasicHeader(
                hasArrow = true,
                onClickBack = { onClickBack(groupId) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                  members
                ) { member ->
                    Swiper(onDelete = { onDelete(member.userId!!)}, onChangeLeader = {onChangeLeader(member)}) {
                        ProfileItem(profile = Profile(userId= member.userId!!, name = member.userName, hospital = member.hospitalName), type = "basic")
                    }
                }
            }
        }

    }

}

@Preview
@Composable
private fun MemberManagePreviewScreen() {
//    MemberManageScreen(
//        groupId = 0,
//        onDelete = {},
//        onClickBack = {},
//        onChangeLeader = {},
//    )
}