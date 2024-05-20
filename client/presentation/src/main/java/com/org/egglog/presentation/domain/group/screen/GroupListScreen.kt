package com.org.egglog.presentation.domain.group.screen

import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.org.egglog.domain.group.model.Group
import com.org.egglog.presentation.component.atoms.buttons.GroupButton
import com.org.egglog.presentation.component.atoms.buttons.HalfBigButton
import com.org.egglog.presentation.component.atoms.buttons.MiddleButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.inputs.PassInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.group.viewmodel.GroupListSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.GroupListViewModel
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.AddBox
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.widthPercent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    groupListViewModel: GroupListViewModel = hiltViewModel(),
    onNavigateToGroupDetailScreen: (groupId: Long) -> Unit,
) {
    val groupListState = groupListViewModel.collectAsState().value
    val context = LocalContext.current
    val showBottomSheet by groupListViewModel.showBottomSheet.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("groupList", "왔니")
//        groupListViewModel.getGroupList()
    }

    groupListViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GroupListSideEffect.Toast -> Toast.makeText(
                context, sideEffect.message, Toast.LENGTH_SHORT
            ).show()

            GroupListSideEffect.NavigateToSettingScreen -> {
                context.startActivity(Intent(
                    context, SettingActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }

            GroupListSideEffect.NavigateToHomeScreen -> {
                context.startActivity(Intent(
                    context, MainActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }

            GroupListSideEffect.NavigateToCommunityScreen -> {
                context.startActivity(Intent(
                    context, CommunityActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }

            GroupListSideEffect.NavigateToCalendarScreen -> {
                context.startActivity(Intent(
                    context, MyCalendarActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
    }


    val isLoading by groupListViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    GroupListScreen(
        groupList = groupListState.groupList,
        selectedIdx = groupListState.selectedIdx,
        onSelectedIdx = groupListViewModel::onSelectedIdx,
        showBottomSheet = showBottomSheet,
        setShowBottomSheet = groupListViewModel::setShowBottomSheet,
        groupName = groupListState.groupName,
        groupPassword = groupListState.groupPassword,
        onGroupNameChange = groupListViewModel::onChangeGroupName,
        onGroupPasswordChange = groupListViewModel::onChangeGroupPassword,
        createGroup = groupListViewModel::createGroup,
        onClickGroup = { groupId: Long -> onNavigateToGroupDetailScreen(groupId) },
        swipeRefreshState = swipeRefreshState,
        refreshSomething = groupListViewModel::refreshSomething,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupListScreen(
    groupList: List<Group>,
    selectedIdx: Int,
    onSelectedIdx: (Int) -> Unit,
    showBottomSheet: Boolean,
    setShowBottomSheet: (Boolean) -> Unit,
    groupName: String,
    groupPassword: String,
    onGroupNameChange: (String) -> Unit,
    onGroupPasswordChange: (String) -> Unit,
    createGroup: () -> Unit,
    onClickGroup: (groupId: Long) -> Unit,
    swipeRefreshState: SwipeRefreshState,
    refreshSomething: () -> Unit
) {

    Column(
        modifier = Modifier
            .background(NaturalWhite)
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = refreshSomething,
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    backgroundColor = NaturalBlack,
                    contentColor = NaturalWhite
                )
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                NoticeHeader(title = "그룹", horizontalPadding = 20, verticalPadding = 10, hasNotice = false)
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(groupList) { group ->
                        GroupButton(
                            onClick = { onClickGroup(group.groupId) },
                            groupMaster = group.admin,
                            groupName = group.groupName,
                            memberCnt = group.memberCount,
                            groupId = group.groupId,
                            groupImage = group.groupImage
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        MiddleButton(
                            onClick = {
                                Log.d("groupList", "클릭")
                                setShowBottomSheet(true)
                            }, colors = ButtonColors(
                                contentColor = Warning25,
                                containerColor = Warning300,
                                disabledContentColor = Gray25,
                                disabledContainerColor = Gray300
                            )
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                Arrangement.SpaceBetween,
                                Alignment.CenterVertically
                            ) {
                                Text(
                                    style = Typography.displayLarge, text = "그룹을 만들고 동료를 초대해보세요"
                                )
                                Icon(
                                    AddBox,
                                    Modifier.size(24.widthPercent(LocalContext.current).dp),
                                    NaturalWhite
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }

        }

        BottomNavigator(selectedItem = selectedIdx, onItemSelected = { onSelectedIdx(it) })
    }


    if (showBottomSheet) {
        BottomSheet(
            height = 300.dp,
            showBottomSheet = showBottomSheet,
            onDismiss = { setShowBottomSheet(false) },
        ) {
            BottomSheetContent(
                groupName = groupName,
                groupPassword = groupPassword,
                onGroupNameChange = onGroupNameChange,
                onGroupPasswordChange = onGroupPasswordChange,
                createGroup = createGroup,
                setShowBottomSheet = setShowBottomSheet,
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    groupName: String,
    groupPassword: String,
    onGroupNameChange: (String) -> Unit,
    onGroupPasswordChange: (String) -> Unit,
    createGroup: () -> Unit,
    setShowBottomSheet: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.width(320.widthPercent(context).dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "그룹 이름을 입력해주세요", style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            SingleInput(
                text = groupName,
                onValueChange = onGroupNameChange,
                focusManager = focusManager,
                placeholder = "그룹이름을 입력하세요",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "비밀번호를 입력해주세요", style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            PassInput(pin = groupPassword, onValueChange = onGroupPasswordChange)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.width(320.widthPercent(context).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HalfBigButton(
                modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                    setShowBottomSheet(false)
                }, colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold), text = "취소"
                )
            }

            HalfBigButton(
                modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                    createGroup()
                    setShowBottomSheet(false)
                }, colors = ButtonColors(
                    contentColor = NaturalWhite,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold), text = "등록"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Preview
@Composable
fun BottomSheetPreviewScreen() {
//    BottomSheetContent()
}

@Preview
@Composable
fun GroupListPreviewScreen() {
    val groupList = listOf(
        Group(groupId = 1, admin = "김다희", groupName = "호남 향우회", memberCount = 2, groupImage = 1),
        Group(groupId = 2, admin = "김다희", groupName = "호남 향우회", memberCount = 2, groupImage = 1),
        Group(groupId = 3, admin = "김다희", groupName = "호남 향우회", memberCount = 2, groupImage = 1),
        Group(groupId = 4, admin = "김다희", groupName = "호남 향우회", memberCount = 2, groupImage = 1),
        Group(groupId = 5, admin = "김다희", groupName = "호남 향우회", memberCount = 2, groupImage = 1),
    )
}

