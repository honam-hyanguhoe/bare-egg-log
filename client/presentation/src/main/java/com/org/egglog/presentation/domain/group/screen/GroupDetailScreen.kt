package com.org.egglog.presentation.domain.group.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.datatransport.runtime.scheduling.jobscheduling.Uploader
import com.org.egglog.client.ui.molecules.radioButtons.WorkRadioButton
import com.org.egglog.domain.group.model.Member
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.component.atoms.buttons.GroupProfileButton
import com.org.egglog.presentation.component.atoms.buttons.HalfBigButton
import com.org.egglog.presentation.component.atoms.buttons.ThinButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.inputs.PassInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.atoms.wheelPicker.CustomDatePicker
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.calendars.GroupCalenar
import com.org.egglog.presentation.component.organisms.calendars.WeeklyCalendar
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.component.organisms.dialogs.WebViewDialog
import com.org.egglog.presentation.domain.group.viewmodel.FileUploadSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.FileUploadViewModel
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.ArrowDown
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate
import java.util.Calendar
import kotlin.reflect.KFunction1

@Composable
fun GroupDetailScreen(
    groupDetailViewModel: GroupDetailViewModel = hiltViewModel(),
    fileUploadViewModel: FileUploadViewModel = hiltViewModel(),
    onNavigateToGroupListScreen: () -> Unit,
    onNavigateToMemberManageScreen: (groupId: Long) -> Unit,
    groupId: Long
) {
    val groupDetailState = groupDetailViewModel.collectAsState().value
    val fileUploadState = fileUploadViewModel.collectAsState().value
    val context = LocalContext.current
    val selected = groupDetailViewModel.selected
    val showBottomSheet by groupDetailViewModel.showBottomSheet.collectAsState()
    val showUploadBottomSheet by groupDetailViewModel.showUploadBottomSheet.collectAsState()
    val showDateBottomSheet by groupDetailViewModel.showDateBottomSheet.collectAsState()
    val showSecondUploadBottomSheet by groupDetailViewModel.showSecondUploadBottomSheet.collectAsState()
    val selectedDate = fileUploadViewModel.selectedDate

    val getFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                fileUploadViewModel.uploadFile(context, uri)
            }
        }
    )

    groupDetailViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GroupDetailSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fileUploadViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FileUploadSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        Log.d("groupDetailScreen", "가져올게")
        groupDetailViewModel.getGroupDuty()
        groupDetailViewModel.getMyWork(LocalDate.now())
        groupDetailViewModel.initSelectedMembers()
    }

    LaunchedEffect(selected.value) {
        groupDetailViewModel.setSelected(selected.value)
        Log.d("groupDetail", "radio ${selected.value}")
    }



    GroupDetailScreen(
        groupId = groupId,
        groupName = groupDetailState.groupInfo.groupName,
        isAdmin = groupDetailState.groupInfo.isAdmin,
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
        myWorkList = groupDetailState.myWorkList,
        groupWorkList = groupDetailState.groupWorkList,

        showBottomSheet = showBottomSheet,
        setShowBottomSheet = groupDetailViewModel::setShowBottomSheet,

        showDateBottomSheet = showDateBottomSheet,
        setShowDateBottomSheet = groupDetailViewModel::setShowDateBottomSheet,

        showUploadBottomSheet = showUploadBottomSheet,
        setShowUploadBottomSheet = groupDetailViewModel::onClickUpload,

        showSecondUploadBottomSheet = showSecondUploadBottomSheet,
        setShowSecondUploadBottomSheet = groupDetailViewModel::onClickNextUpload,
        // settings
        tempGroupName = groupDetailState.tempGroupName,
        tempGroupPassword = groupDetailState.tempGroupPassword,

        selectedWorkDate=  groupDetailState.selectedWorkDate,
        setSelectedWorkDate = groupDetailViewModel::setSelectedWorkDate,
        getSelectedDateWork = groupDetailViewModel::getSelectedDateWork,

        setSelectedDate = fileUploadViewModel::setSelectedDate,
        customDutyList = fileUploadState.customDutyList,
        onChangeFileDay = fileUploadViewModel::onChangeFileDay,
        onChangeFileEVE = fileUploadViewModel::onChangeFileEVE,
        onChangeFileNIGHT = fileUploadViewModel::onChangeFileNIGHT,
        onChangeFileOFF = fileUploadViewModel::onChangeFileOFF,
        onGroupNameChange = groupDetailViewModel::onChangeGroupName,
        onGroupPasswordChange = groupDetailViewModel::onChangeGroupPassword,
        updateGroup = groupDetailViewModel::updateGroupInfo,
        onClickCancel = groupDetailViewModel::onClickCancel,
        onClickManageMember = { groupId: Long -> onNavigateToMemberManageScreen(groupId) },
        exitGroup = groupDetailViewModel::exitGroup,
        onClickFileUploader = { getFileLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")}
    )
}

@Composable
private fun GroupDetailScreen(
    groupId: Long,
    groupName: String,
    isAdmin: Boolean = false,
    memberCount: Int,
    adminName: String,
    onClickBack: () -> Unit,
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    onDateClick: (WeeklyUiModel.Date) -> Unit,
    selected: MutableState<String>,
    setSelectedDate : (LocalDate?) -> Unit,
    selectedDuty: List<Member>,
    copyInvitationLink: (Context) -> Unit,
    toggleMemberSelection: KFunction1<Member, Unit>,
    selectedMembers: List<Member>,
    myWorkList: Map<String, Map<String, String>>,
    groupWorkList: Map<String, Map<String, String>>,
    showBottomSheet: Boolean,
    setShowBottomSheet: (Boolean) -> Unit,
    showUploadBottomSheet: Boolean,
    setShowUploadBottomSheet: (Boolean) -> Unit,
    showSecondUploadBottomSheet: Boolean,
    setShowSecondUploadBottomSheet: (Boolean) -> Unit,

    getSelectedDateWork : () -> Unit,
    selectedWorkDate: LocalDate?,
    setSelectedWorkDate : (LocalDate?) -> Unit,
    showDateBottomSheet: Boolean,
    setShowDateBottomSheet: (Boolean) -> Unit,
    // 그룹 설정
    tempGroupName: String,
    tempGroupPassword: String,
    customDutyList : Map<String, String>,
    onChangeFileDay: (String) -> Unit,
    onChangeFileEVE: (String) -> Unit,
    onChangeFileNIGHT: (String) -> Unit,
    onChangeFileOFF: (String) -> Unit,
    onGroupNameChange: (String) -> Unit,
    onGroupPasswordChange: (String) -> Unit,
    updateGroup: () -> Unit,
    onClickCancel: () -> Unit,
    onClickManageMember: (groupId: Long) -> Unit,
    exitGroup: () -> Unit,
    onClickFileUploader: () -> Unit
) {
    val tempLabels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")
    val options = listOf("그룹 설정", "그룹원 설정", "그룹 나가기")
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val openDialogExit = remember { mutableStateOf(false) }
    val openDialogBoss = remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val focusRequester = remember { FocusRequester() }


    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = NaturalWhite)
            .systemBarsPadding()
            .focusRequester(focusRequester),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            BasicHeader(
                hasArrow = true,
                hasMore = true,
                hasInvitationButton = true,
                onClickBack = onClickBack,
                onClickLink = {
                    focusRequester.requestFocus()
                    copyInvitationLink(context)
                },
                options = options,
                onSelect = {
                    selectedOption = it
                    Log.d("menus", "it $it")
                    if (it == "그룹 설정") {
                        if (isAdmin) {
                            setShowBottomSheet(true)
                        } else {
                            Toast.makeText(context, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else if (it == "그룹원 설정") {
                        Log.d("screen", "그룹원 설정 페이지 이동")
                        if (isAdmin) {
                            onClickManageMember(groupId)
                        } else {
                            Toast.makeText(context, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else if (it == "그룹 나가기") {
                        if (isAdmin) {
                            if (memberCount == 1) {
                                openDialogExit.value = true
                            } else {
                                openDialogBoss.value = true
                            }
                        } else {
                            openDialogExit.value = true
                        }
                    }
                }
            )
        }
        item {
            GroupInfoCard(
                groupName = groupName,
                memberCount = memberCount,
                adminName = adminName,
                setShowUploadBottomSheet = setShowUploadBottomSheet
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
            MemberCalendar(myWorkList = myWorkList, groupWorkList = groupWorkList, setShowDateBottomSheet = setShowDateBottomSheet, selectedWorkDate = selectedWorkDate)
        }
    }

    when {
        openDialogExit.value -> {
            Dialog(
                onDismissRequest = { openDialogExit.value = false },
                onConfirmation = {
                    exitGroup()
                    openDialogExit.value = false
                    onClickBack()
                },
                dialogTitle = "그룹을 나가시겠습니까?",
                dialogText = "그룹을 나가면 해당 그룹의 모든 활동 및 업데이트를 더 이상 받을 수 없습니다."
            )
        }

        openDialogBoss.value -> {
            Dialog(
                onDismissRequest = { openDialogBoss.value = false },
                onConfirmation = { openDialogBoss.value = false },
                dialogTitle = "그룹장은 탈퇴할 수 없습니다.",
                dialogText = "새 그룹장을 선택 후 다시 시도해주세요."
            )
        }
    }

    if (showBottomSheet) {
        BottomSheet(
            height = 300.dp,
            showBottomSheet = showBottomSheet,
            onDismiss = { setShowBottomSheet(false) },
        ) {
            GroupBottomSheetContent(
                tempGroupName = tempGroupName,
                tempGroupPassword = tempGroupPassword,
                onGroupNameChange = onGroupNameChange,
                onGroupPasswordChange = onGroupPasswordChange,
                updateGroup = updateGroup,
                setShowBottomSheet = setShowBottomSheet,
                onClickCancel = onClickCancel,
            )
        }
    }

    // 근무표 등록 바텀시트
    if (showUploadBottomSheet) {
        BottomSheet(
            height = 300.dp,
            showBottomSheet = showUploadBottomSheet,
            onDismiss = { setShowUploadBottomSheet(false) },
        ) {
            SelectDateBottomSheetContent(
                onDateTimeSelected = { date ->
                    selectedDate = date
                    setSelectedDate(date)
//                    Log.d("upload", "selected ${selectedDate}")
                },
                onClickCancel = { setShowUploadBottomSheet(false) },
                onClickNext = {
                    setShowUploadBottomSheet(false)
                    setShowSecondUploadBottomSheet(true)
                }
            )
        }
    }

    if (showDateBottomSheet) {
        BottomSheet(
            height = 300.dp,
            showBottomSheet = showDateBottomSheet,
            onDismiss = { setShowDateBottomSheet(false) },
        ) {
            SelectDateBottomSheetContent(
                onDateTimeSelected = { date ->
                    selectedDate = date
                    setSelectedWorkDate(date)
                },
                onClickCancel = { setShowDateBottomSheet(false) },
                onClickNext = {
                    getSelectedDateWork()
                    setShowDateBottomSheet(false)
                },
                title = "조회할 근무 날짜를 선택하세요",
                buttonText = "완료"
            )
        }
    }

    if (showSecondUploadBottomSheet) {
        BottomSheet(
            height = 450.dp,
            showBottomSheet = showBottomSheet,
            onDismiss = { setShowSecondUploadBottomSheet(false) },
        ) {
            UploadFileBottomSheetContent(
                customDutyList = customDutyList,
                onChangeFileDay = onChangeFileDay,
                onChangeFileEVE = onChangeFileEVE,
                onChangeFileNIGHT = onChangeFileNIGHT,
                onChangeFileOFF = onChangeFileOFF,
                onClickCancel = { setShowSecondUploadBottomSheet(false) },
                onClickUploadFile = {
                    onClickFileUploader()
                    setShowSecondUploadBottomSheet(false)
                }
            )
        }
    }

}

@Composable
fun UploadFileBottomSheetContent(
    customDutyList : Map<String, String>,
    onChangeFileDay: (String) -> Unit,
    onChangeFileEVE: (String) -> Unit,
    onChangeFileNIGHT: (String) -> Unit,
    onChangeFileOFF: (String) -> Unit,
    onClickUploadFile: () -> Unit,
    onClickCancel: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .width(320.widthPercent(context).dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "근무를 지정할 표기를 작성해주세요", style = Typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "DAY", style = Typography.bodyLarge, textAlign = TextAlign.Start)
                    SingleInput(
                        text = customDutyList["DAY"] ?: "",
                        onValueChange = onChangeFileDay,
                        focusManager = focusManager,
                        placeholder = "DAY",
                        modifier = Modifier.width(200.widthPercent(context).dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "EVE", style = Typography.bodyLarge, textAlign = TextAlign.Start)
                    SingleInput(
                        text = customDutyList["EVE"] ?: "",
                        onValueChange = onChangeFileEVE,
                        focusManager = focusManager,
                        placeholder = "EVE",
                        modifier = Modifier.width(200.widthPercent(context).dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "NIGHT", style = Typography.bodyLarge, textAlign = TextAlign.Start)
                    SingleInput(
                        text = customDutyList["NIGHT"] ?: "",
                        onValueChange = onChangeFileNIGHT,
                        focusManager = focusManager,
                        placeholder = "NIGHT",
                        modifier = Modifier.width(200.widthPercent(context).dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "OFF", style = Typography.bodyLarge, textAlign = TextAlign.Start)
                    SingleInput(
                        text = customDutyList["OFF"] ?: "",
                        onValueChange = onChangeFileOFF,
                        focusManager = focusManager,
                        placeholder = "OFF",
                        modifier = Modifier.width(200.widthPercent(context).dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                    )
                }
            }
            Row(
                modifier = Modifier.width(320.widthPercent(context).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HalfBigButton(
                    modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                        onClickCancel()
                    }, colors = ButtonColors(
                        contentColor = Gray25,
                        containerColor = Gray300,
                        disabledContentColor = Gray25,
                        disabledContainerColor = Gray300
                    )
                ) {
                    Text(
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        text = "취소"
                    )
                }
                HalfBigButton(
                    modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                        onClickUploadFile()
                    }, colors = ButtonColors(
                        contentColor = NaturalWhite,
                        containerColor = Warning300,
                        disabledContentColor = Gray25,
                        disabledContainerColor = Gray300
                    )
                ) {
                    Text(
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        text = "근무표 업로드"
                    )
                }
            }
        }
    }
}

@Composable
fun SelectDateBottomSheetContent(
    title : String = "업로드할 근무 날짜를 선택하세요",
    onDateTimeSelected: (LocalDate) -> Unit,
    onClickNext: () -> Unit,
    onClickCancel: () -> Unit,
    buttonText : String = "다음"
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .width(320.widthPercent(context).dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title, style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomDatePicker { date ->
                onDateTimeSelected(date)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.width(320.widthPercent(context).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HalfBigButton(
                    modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                        onClickCancel()
                    }, colors = ButtonColors(
                        contentColor = Gray25,
                        containerColor = Gray300,
                        disabledContentColor = Gray25,
                        disabledContainerColor = Gray300
                    )
                ) {
                    Text(
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        text = "취소"
                    )
                }

                HalfBigButton(
                    modifier = Modifier.width(150.widthPercent(context).dp), onClick = {
                        onClickNext()
                    }, colors = ButtonColors(
                        contentColor = NaturalWhite,
                        containerColor = Warning300,
                        disabledContentColor = Gray25,
                        disabledContainerColor = Gray300
                    )
                ) {
                    Text(
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        text = buttonText
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun GroupBottomSheetContent(
    tempGroupName: String,
    tempGroupPassword: String,
    onGroupNameChange: (String) -> Unit,
    onGroupPasswordChange: (String) -> Unit,
    updateGroup: () -> Unit,
    setShowBottomSheet: (Boolean) -> Unit,
    onClickCancel: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.width(320.widthPercent(context).dp),
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "그룹 이름을 입력해주세요", style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            SingleInput(
                text = tempGroupName,
                onValueChange = onGroupNameChange,
                focusManager = focusManager,
                placeholder = "그룹이름을 입력하세요",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "비밀번호를 입력해주세요", style = Typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            PassInput(pin = tempGroupPassword, onValueChange = onGroupPasswordChange)
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
                    onClickCancel()
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
                    updateGroup()
                    setShowBottomSheet(false)
                }, colors = ButtonColors(
                    contentColor = NaturalWhite,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold), text = "수정"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
private fun GroupInfoCard(
    groupName: String,
    memberCount: Int,
    adminName: String,
    setShowUploadBottomSheet: (Boolean) -> Unit
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
                onClick = { setShowUploadBottomSheet(true) }, colors = ButtonColors(
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
    myWorkList: Map<String, Map<String, String>>,
    groupWorkList: Map<String, Map<String, String>>,
    setShowDateBottomSheet : (Boolean) -> Unit,
    selectedWorkDate: LocalDate?
) {
    val currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

    currentYear.value = selectedWorkDate!!.year
    currentMonth.value = selectedWorkDate.monthValue
    Log.d("date group", "current $currentYear  $currentMonth")
//    val workList = myWorkList + groupWorkList

    Column(
        modifier = Modifier
            .width(340.widthPercent(LocalContext.current).dp)
            .padding(8.dp, 15.dp, 8.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${selectedWorkDate!!.year}.${selectedWorkDate.monthValue}", style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            
            CustomIconButton(size = 24.dp, imageVector = ArrowDown, color = NaturalBlack, onClick = { setShowDateBottomSheet(true) })
        }
        Spacer(modifier = Modifier.height(10.dp))
        GroupCalenar(currentYear = currentYear, currentMonth = currentMonth, myWorkList+groupWorkList)
    }
}

@Preview
@Composable
fun GroupDetailPreviewScreen() {
//    GroupDetailScreen()
//    MemberCalendar(myWorkList = emptyMap(), groupWorkList = emptyMap(), setShowDateBottomSheet= {})
}