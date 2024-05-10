package com.org.egglog.presentation.domain.group.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.presentation.component.atoms.buttons.GroupButton
import com.org.egglog.presentation.component.atoms.buttons.HalfBigButton
import com.org.egglog.presentation.component.atoms.buttons.HalfMiddleButton
import com.org.egglog.presentation.component.atoms.buttons.MiddleButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.inputs.PassInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.group.viewmodel.GroupListSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.GroupListViewModel
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.main.screen.DutyCard
import com.org.egglog.presentation.domain.main.screen.RemainCard
import com.org.egglog.presentation.domain.main.screen.StaticsCard
import com.org.egglog.presentation.domain.main.viewModel.StaticSideEffect
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.theme.Gray25
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.Gray600
import com.org.egglog.presentation.theme.Gray800
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Success400
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning25
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.theme.Warning600
import com.org.egglog.presentation.utils.AddBox
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GroupListScreen(
    groupListViewModel: GroupListViewModel = hiltViewModel()
) {
    val groupListState = groupListViewModel.collectAsState().value
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val onDismiss = { showBottomSheet = false }

    groupListViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GroupListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()

            GroupListSideEffect.NavigateToSettingScreen -> {
                context.startActivity(
                    Intent(
                        context, SettingActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            GroupListSideEffect.NavigateToHomeScreen -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            GroupListSideEffect.NavigateToCommunityScreen -> {
                context.startActivity(
                    Intent(
                        context, CommunityActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            GroupListSideEffect.NavigateToCalendarScreen -> {
                context.startActivity(
                    Intent(
                        context, SettingActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

    GroupListScreen(
        groupList = groupListState.groupList,
        selectedIdx = groupListState.selectedIdx,
        onSelectedIdx = groupListViewModel::onSelectedIdx,
        showBottomSheet = showBottomSheet,
        setShowBottomSheet = { showBottomSheet = it }
    )
}

@Composable
private fun GroupListScreen(
    groupList: List<Group>,
    selectedIdx: Int,
    onSelectedIdx: (Int) -> Unit,
    showBottomSheet: Boolean,
    setShowBottomSheet: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(NaturalWhite)
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
//                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            NoticeHeader(title = "그룹", horizontalPadding = 20, verticalPadding = 10)
            Spacer(modifier = Modifier.height(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(groupList) { group ->
                    GroupButton(
                        onClick = { },
                        groupMaster = group.master,
                        groupName = group.name,
                        memberCnt = group.memberCount,
                        groupId = group.id,
                        groupImage = group.image
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item{
                    Spacer(modifier = Modifier.height(10.dp))

                    MiddleButton(
                        onClick = {
                            Log.d("groupList", "클릭")
                            setShowBottomSheet(true)
                        },
                        colors = ButtonColors(
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
                                style = Typography.displayLarge,
                                text = "그룹을 만들고 동료를 초대해보세요"
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

        Box(
            modifier = Modifier
                .fillMaxHeight(1f)
                .fillMaxWidth()
        ) {
            BottomNavigator(selectedItem = selectedIdx, onItemSelected = { onSelectedIdx(it) })
        }
    }

    val pin = remember { mutableStateOf("") }
    val text1 = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    if (showBottomSheet) {
        BottomSheet(
            height = 300.dp,
            showBottomSheet = showBottomSheet,
            onDismiss = { setShowBottomSheet(false) },
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .border(1.dp, NaturalBlack),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(320.widthPercent(context).dp),
//                        .border(1.dp, Success400),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "그룹 이름을 입력해주세요",
                        style = Typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SingleInput(
                        text = text1.value,
                        onValueChange = { text1.value = it },
                        focusManager = focusManager,
                        placeholder = "사번 입력",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "비밀번호를 입력해주세요",
                        style = Typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    PassInput(pin = pin.value, onValueChange = { pin.value = it })
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .width(320.widthPercent(context).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HalfBigButton(
                        modifier = Modifier.width(150.widthPercent(context).dp),
                        onClick = { Log.d("clicked: ", "clicked!!!!") },
                        colors = ButtonColors(
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
                        modifier = Modifier.width(150.widthPercent(context).dp),
                        onClick = { Log.d("clicked: ", "clicked!!!!") },
                        colors = ButtonColors(
                            contentColor = NaturalWhite,
                            containerColor = Warning300,

                            disabledContentColor = Gray25,
                            disabledContainerColor = Gray300
                        )
                    ) {
                        Text(
                            style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            text = "등록"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }
}


@Preview
@Composable
fun GroupListPreviewScreen() {
    val groupList = listOf(
        Group(id = 1, master = "김다희", name = "호남향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네친구들", memberCount = 3, image = 3)
    )
//
//    GroupListScreen(
//        groupList = groupList,
//        selectedIdx = 1,
//        onSelectedIdx = {}
//    )
}

