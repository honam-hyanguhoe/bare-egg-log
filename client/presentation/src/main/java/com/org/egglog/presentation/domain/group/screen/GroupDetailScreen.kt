package com.org.egglog.presentation.domain.group.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.client.ui.molecules.radioButtons.WorkRadioButton
import com.org.egglog.domain.group.model.Member
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.GroupProfileButton
import com.org.egglog.presentation.component.atoms.buttons.ThinButton
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.calendars.GroupCalenar
import com.org.egglog.presentation.component.organisms.calendars.WeeklyCalendar
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate
import java.util.Calendar
import kotlin.reflect.KFunction1

@Composable
fun GroupDetailScreen(
    groupDetailViewModel: GroupDetailViewModel = hiltViewModel(),
    onNavigateToGroupListScreen: () -> Unit,
    groupId: Long?
) {
    val groupDetailState = groupDetailViewModel.collectAsState().value
    val context = LocalContext.current
    val selected = groupDetailViewModel.selected


    LaunchedEffect(selected.value) {
        groupDetailViewModel.setSelected(selected.value)
        Log.d("groupDetail", "radio ${selected.value}")
    }

    GroupDetailScreen(
        groupName = groupDetailState.groupInfo.groupName,
        memberCount = (groupDetailState.groupInfo.groupMembers?.size?.plus(1)) ?: 1,
        adminName = groupDetailState.groupInfo.admin?.userName ?: "",
        onClickBack = onNavigateToGroupListScreen,
        calendarUiModel = groupDetailState.currentWeekDays,
        onPrevClick = groupDetailViewModel::onPrevClick,
        onNextClick = groupDetailViewModel::onNextClick,
        startDate = groupDetailState.startDate,
        onDateClick = groupDetailViewModel::onDateClick,
        selected = selected,
        selectedDuty = groupDetailState.selectedDuty,
        // 초대링크
        copyInvitationLink = groupDetailViewModel::copyInvitationLink,
        toggleMemberSelection = groupDetailViewModel::toggleMemberSelection,
        selectedMembers = groupDetailState.selectedMembers,
        myWorkList = groupDetailState.myWorkList
    )
}

@Composable
private fun GroupDetailScreen(
    groupName: String,
    memberCount: Int,
    adminName: String,
    onClickBack: () -> Unit,
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    onDateClick: (WeeklyUiModel.Date) -> Unit,
    selected: MutableState<String>,
    selectedDuty: List<Member>,
    copyInvitationLink: (Context) -> Unit,
    toggleMemberSelection: KFunction1<Member, Unit>,
    selectedMembers: List<Member>,
    myWorkList : Map<String, Map<String, String>>
) {
    val tempLabels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")
    val options = listOf("그룹 설정", "그룹원 설정", "그룹 나가기")
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NaturalWhite),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            BasicHeader(
                hasArrow = true,
                hasMore = true,
                hasInvitationButton = true,
                onClickBack = onClickBack,
                onClickLink = { copyInvitationLink(context) },
                options = options
            )
        }
        item {
            GroupInfoCard(
                groupName = groupName,
                memberCount = memberCount,
                adminName = adminName,

                )
        }
        item {
            Spacer(modifier = Modifier.height(5.dp))
            DutyCard(
                startDate = startDate,
                calendarUiModel = calendarUiModel,
                onPrevClick = onPrevClick,
                onNextClick = onNextClick,
                onDateClick = onDateClick
            )
        }
        item {
            Spacer(modifier = Modifier.height(5.dp))
            MembersCard(
                selected = selected,
                selectedDuty = selectedDuty,
                selectedMembers = selectedMembers,
                toggleMemberSelection = toggleMemberSelection
            )
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
            MemberCalendar(myWorkList = myWorkList)
        }
    }
}

@Composable
private fun GroupInfoCard(
    groupName: String, memberCount: Int, adminName: String
) {
    val context = LocalContext.current
    Column(
        Modifier
            .width(340.widthPercent(context).dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val yOffset = size.height - strokeWidth / 2
                drawLine(
                    Gray300,
                    start = Offset(0f, yOffset),
                    end = Offset(size.width, yOffset),
                    strokeWidth = strokeWidth
                )
            }
            .padding(8.dp, 15.dp, 8.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(
            modifier = Modifier
                .width(320.widthPercent(context).dp)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                text = "$groupName 모여라"
            )
            Text(
                style = Typography.displayLarge.copy(fontWeight = FontWeight.Medium),
                text = "멤버 $memberCount · 그룹장 $adminName"
            )

            Spacer(modifier = Modifier.height(20.dp))
            ThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                ), modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        50.heightPercent(
                            LocalContext.current
                        ).dp
                    )
            ) {
                Text(
                    style = Typography.bodyLarge, text = "근무표 등록하기"
                )
            }
        }
    }
}

@Composable
fun DutyCard(
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    onDateClick: (WeeklyUiModel.Date) -> Unit,
) {
    val context = LocalContext.current
    val tempLabels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")
    Column(
        modifier = Modifier
            .width(340.widthPercent(context).dp)
            .background(color = NaturalWhite, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {

        WeeklyCalendar(
            type = "group",
            startDate = startDate,
            calendarUiModel = calendarUiModel,
            labels = tempLabels,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick,
            width = 320,
            backgroundColor = NaturalWhite,
            contentsColor = White,
            containerColor = White,
            onDateClick = onDateClick
        )
    }
}


@Composable
private fun MembersCard(
    selected: MutableState<String>,
    selectedDuty: List<Member>,
    toggleMemberSelection: KFunction1<Member, Unit>,
    selectedMembers: List<Member>
) {
    val context = LocalContext.current
    val radioList = arrayListOf("Day", "Eve", "Night", "Off", "Etc")
    Column(
        modifier = Modifier
            .width(340.widthPercent(context).dp)
            .padding(8.dp, 15.dp, 8.dp, 0.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "함께 일할 동료는 누구일까요?",
            style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocalImageLoader(
                imageUrl = R.drawable.small_fry,
                modifier = Modifier.size(12.widthPercent(context).dp)
            )
            Text(
                text = "근무 태그를 선택하여 함께 근무할 동료를 찾아보세요 (최대 2명)",
                style = Typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        WorkRadioButton(radioList = radioList, selected = selected)
        if (selectedDuty.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(108.heightPercent(context).dp)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                selectedDuty.chunked(5).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowItems.forEach { member ->
                            GroupProfileButton(
                                onClick = {
                                    toggleMemberSelection(member)
                                },
                                userInfo = member,
                                isSelected = selectedMembers.contains(member),
                                isMine = false,
                            )
                        }
                        if (rowItems.size < 5) {
                            repeat(5 - rowItems.size) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberCalendar(
    myWorkList : Map<String, Map<String, String>>
) {
    val currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

    // 내 근무 일정
    Log.d("myWork", "ggg $myWorkList")

    // 선택한 그룹원 일정
    val groupWorkList = mapOf(
        "김형민" to mapOf(
            "2024-05-01" to "DAY",
            "2024-05-02" to "EVE",
            "2024-05-03" to "OFF",
            "2024-05-04" to "NONE",
            "2024-05-05" to "DAY",
            "2024-05-06" to "DAY",
            "2024-05-07" to "OFF",
            "2024-05-08" to "EVE"
        ), "김아현" to mapOf(
            "2024-05-01" to "DAY",
            "2024-05-02" to "EVE",
            "2024-05-03" to "OFF",
            "2024-05-04" to "NONE",
            "2024-05-05" to "DAY",
            "2024-05-06" to "DAY",
            "2024-05-07" to "OFF",
            "2024-05-08" to "EVE"
        )
    )
    // 나 + 그룹원 일정 합쳐서 인수로 전달s
    val workList = myWorkList + groupWorkList
    Log.d("myWork", "groupWorkList $groupWorkList")
    Column(
        modifier = Modifier
            .width(340.widthPercent(LocalContext.current).dp)
            .padding(8.dp, 15.dp, 8.dp, 0.dp)
//            .border(1.dp, NaturalBlack)
    ) {
        Text(
            text = "2024. 04", style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(10.dp))
        GroupCalenar(currentYear = currentYear, currentMonth = currentMonth, workList)
    }
}

@Preview
@Composable
fun GroupDetailPreviewScreen() {
//    GroupDetailScreen()
}