package com.org.egglog.presentation.domain.group.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.client.data.UserInfo
import com.org.egglog.client.ui.molecules.radioButtons.WorkRadioButton
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.ProfileButton
import com.org.egglog.presentation.component.atoms.buttons.ThinButton
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.calendars.GroupCalenar
import com.org.egglog.presentation.component.organisms.calendars.WeeklyCalendar
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate
import java.util.Calendar

@Composable
fun GroupDetailScreen(
    groupDetailViewModel: GroupDetailViewModel = hiltViewModel(),
    onNavigateToGroupListScreen: () -> Unit,
    groupId: Long?
) {
    val groupDetailState = groupDetailViewModel.collectAsState().value
    val context = LocalContext.current

    GroupDetailScreen(
        groupName = groupDetailState.groupInfo.groupName,
        memberCount = (groupDetailState.groupInfo.groupMembers?.size?.plus(1)) ?: 1,
        adminName = groupDetailState.groupInfo.admin?.userName ?: "",
        onClickBack = onNavigateToGroupListScreen,
        calendarUiModel = groupDetailState.currentWeekDays,
        onPrevClick = groupDetailViewModel::onPrevClick,
        onNextClick = groupDetailViewModel::onNextClick,
        startDate = groupDetailState.startDate,
        onDateClick = groupDetailViewModel::onDateClick
    )
}

@Composable
private fun GroupDetailScreen(
    groupName : String,
    memberCount : Int,
    adminName : String,
    onClickBack : () -> Unit,
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    onDateClick: (WeeklyUiModel.Date) -> Unit,
) {
//    val dataSource = WeeklyDataSource()
//    val calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    val tempLabels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")
//    val scrollState = rememberScrollState()
    val options = listOf("그룹 설정", "그룹원 설정", "그룹 나가기")
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NaturalWhite),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            BasicHeader(
                hasArrow = true, hasMore = true, hasInvitationButton = true, onClickBack = onClickBack, options = options
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
            MembersCard()
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
            MemberCalendar()
        }
    }
}

@Composable
private fun GroupInfoCard(
    groupName : String,
    memberCount : Int,
    adminName : String
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
            .padding(8.dp, 15.dp, 8.dp, 0.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .width(320.widthPercent(context).dp)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold), text = "$groupName 모여라"
            )
            Text(style = Typography.displayLarge.copy(fontWeight = FontWeight.Medium), text = "멤버 $memberCount · 그룹장 $adminName")

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
//            .border(1.dp, NaturalBlack)
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
private fun MembersCard() {
    val context = LocalContext.current
    val radioList = arrayListOf("Day", "Eve", "Night", "Off", "Etc")
    val selected = remember { mutableStateOf("Day") }
    val tempUserInfo = UserInfo(
        profileImgUrl = "https://cataas.com/cat",
        userName = "김호남",
        empNo = "",
        userId = 0,
        userEmail = "",
    )
    val memberList = listOf(
        tempUserInfo, tempUserInfo, tempUserInfo, tempUserInfo, tempUserInfo, tempUserInfo
    )

    Column(
        modifier = Modifier
            .width(340.widthPercent(context).dp)
            .padding(8.dp, 15.dp, 8.dp, 0.dp),
//            .border(1.dp, NaturalBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "함께 일할 동료는 누구일까요?", style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
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
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            memberList.chunked(5).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowItems.forEach { member ->
                        ProfileButton(
                            onClick = { /* 클릭 이벤트 처리 */ },
                            userInfo = member,
                            isSelected = false,
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

@Composable
private fun MemberCalendar() {
    val currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

    // 내 근무 일정
    val myWorkList = mapOf(
        "김다희" to mapOf(
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

    Column(
        modifier = Modifier
            .width(340.widthPercent(LocalContext.current).dp)
            .padding(8.dp, 15.dp, 8.dp, 0.dp)
//            .border(1.dp, NaturalBlack)
    ) {
        Text(text = "2024. 04", style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(10.dp))
        GroupCalenar(currentYear = currentYear, currentMonth = currentMonth, workList)

    }


}

@Preview
@Composable
fun GroupDetailPreviewScreen() {
//    GroupDetailScreen()
}