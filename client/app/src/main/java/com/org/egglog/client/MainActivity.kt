package com.org.egglog.client

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.UserInfo
import com.org.egglog.client.ui.atoms.buttons.AuthButton
import com.org.egglog.client.ui.atoms.buttons.BigButton
import com.org.egglog.client.ui.atoms.buttons.FloatingButton
import com.org.egglog.client.ui.atoms.buttons.GroupButton
import com.org.egglog.client.ui.atoms.buttons.HalfBigButton
import com.org.egglog.client.ui.atoms.buttons.HalfMiddleButton
import com.org.egglog.client.ui.atoms.buttons.HalfThinButton
import com.org.egglog.client.ui.atoms.buttons.IconTextButton
import com.org.egglog.client.ui.atoms.buttons.MiddleButton
import com.org.egglog.client.ui.atoms.buttons.ProfileButton
import com.org.egglog.client.ui.atoms.buttons.SettingButton
import com.org.egglog.client.ui.atoms.buttons.ThinButton
import com.org.egglog.client.ui.atoms.cards.ProfileCard
import com.org.egglog.client.ui.atoms.checkbox.CheckBoxRow
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.inputs.MultiInput
import com.org.egglog.client.ui.atoms.inputs.PassInput
import com.org.egglog.client.ui.atoms.inputs.SearchInput
import com.org.egglog.client.ui.atoms.inputs.SingleInput
import com.org.egglog.client.ui.theme.ClientTheme
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.atoms.labels.Labels
import com.org.egglog.client.ui.atoms.toggle.Toggle
import com.org.egglog.client.ui.atoms.wheelPicker.DateTimePicker
import com.org.egglog.client.ui.atoms.wheelPicker.TimePicker
import com.org.egglog.client.ui.molecules.headers.BasicHeader
import com.org.egglog.client.ui.molecules.headers.NoticeHeader
import com.org.egglog.client.ui.molecules.headers.SearchHeader
import com.org.egglog.client.ui.molecules.cards.CommentCard
import com.org.egglog.client.ui.molecules.tabBar.TabBar
import com.org.egglog.client.ui.molecules.infoList.InfoList
import com.org.egglog.client.ui.molecules.postReaction.PostReaction
import com.org.egglog.client.ui.organisms.agreeList.AgreeList
import com.org.egglog.client.ui.molecules.profileButtonList.ProfileButtonList
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.AddBox
import com.org.egglog.client.utils.Logout
import com.org.egglog.client.utils.MySetting
import com.org.egglog.client.utils.Search
import com.org.egglog.client.utils.addFocusCleaner
import java.time.LocalDateTime
import java.time.LocalTime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyAppPreview() {
    ClientTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO, name = "DefaultPreviewLight"
)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
//    LabelTest()
//    ButtonTest()
//    ToggleTest()
//    InputTest()
//    CheckBoxTest()
//    TimePickerTest()
//    BottomSheetTest()
//    AgreeListTest()
//    CardTest()
//    ProfileButtonTest()
//    InfoListTest()
//    CommunityTest()
//    TabBarTest()
//    InfoListTest()
    HeaderTest()
}

@Composable
fun HeaderTest() {
    Column {

        val groupOptions = listOf("그룹 설정", "그룹원 설정", "그룹 나가기")
        var selectedMenuItem by remember { mutableStateOf<String?>(null) }

        val communityOptions = listOf("통합", "엑록병원", "호남향우회")
//        var selectedMenuItem by remember { mutableStateOf<String?>(null) }

        BasicHeader(
            title = "무튼 제목임",
            hasTitle = false,
            hasArrow = true,
            hasLeftClose = false,
            hasClose = false,
            hasInvitationButton = true,
            hasProgressBar = true,
            hasMore = true,
            onClickBack = {},
            onClickLink = {},
            onClickClose = {},
            onClickMenus = {},
            options = groupOptions,
            selectedOption = selectedMenuItem,
            onSelect = {selectedMenuItem = it}
        )

        Spacer(modifier = Modifier.height(30.dp))
        NoticeHeader(
            title = "무튼 제목임",
            hasSearch = true,
            hasLogo = false,
            hasMenu = true,
            onClickSearch = {},
            onClickNotification = {},
            onClickMenus = {},
            options = communityOptions,
            selectedOption = selectedMenuItem,
            onSelect = {selectedMenuItem = it}
        )

        Spacer(modifier = Modifier.height(30.dp))

        SearchHeader()
    }
}

@Composable
fun BottomSheetTest() {
    var showBottomSheet by remember { mutableStateOf<Boolean>(false) }

    fun dismissSheet() {
        showBottomSheet = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(2.dp, NaturalBlack)
    ) {

        Text("hi")

        FloatingButton(
            onClick = { println("fab 클릭") },
            onWorkClick = { println("근무 일정 추가 클릭") },
            onPersonalClick = { println("개인 일정 추가 클릭") },
            onSettingClick = { println("근무 설정 클릭") },
            10.dp,
            30.dp,
        )
    }

}

@Composable
fun BottomSheetContent() {
    Text(text = "BottomSheetContent 테스트")
    Spacer(modifier = Modifier.height(180.dp))
}

@Composable
fun TabBarTest() {
    val titles = listOf("근무", "알람")
    TabBar(
        titles,
        { FirstTabContents() },
        { SecondTabContents() },
    )
}

@Composable
fun FirstTabContents() {
    Column {
        Text("첫번쨰 컨텐츠")
    }
}

@Composable
fun SecondTabContents() {
    Column {
        Text("두번쨰 컨텐츠")
    }
}

@Composable
fun LabelTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Day", size = "big")
                Labels(text = "Eve", size = "big")
                Labels(text = "Night", size = "big")
                Labels(text = "교육", size = "big")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Eve")
                Labels(text = "Off")
                Labels(text = "Night")
                Labels(text = "Eve")
                Labels(text = "보건")
                Labels(text = "휴가")
                Labels(text = "None")
            }
        }
    }
}

@Composable
fun InfoListTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        InfoList()
    }
}

@Composable
fun CommunityTest(modifier: Modifier = Modifier) {
    val postReaction1 = PostReactionInfo(1, 100, 13, 123, true, true, true)
    val postReaction2 = PostReactionInfo(1, 100, 13, isLiked = true, isCommented = true)

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            PostReaction(postReactionInfo = postReaction1)
            PostReaction(postReactionInfo = postReaction2)
            CommentCard(CommentInfo(
                1,
                1,
                "test",
                "전남대병원",
                "익명의 구운란",
                "2023-12-24 13:28:12",
                "https://picsum.photos/300",
                true,
                arrayListOf(
                    CommentInfo(
                        1,
                        2,
                        "test",
                        "전남대병원",
                        "익명의 구운란",
                        "2023-12-24 13:28:12",
                        "https://picsum.photos/300",
                        true
                    )
                )
            ), myUserId = 2, onDeleteClick = { clickedCommentId ->
                Log.d(
                    "답글삭제 클릭: ", "$clickedCommentId clicked!!!"
                )
            }, onRecommentClick = { clickedCommentId ->
                Log.d(
                    "답글달기 클릭: ", "$clickedCommentId clicked!!!"
                )
            })
            CommentCard(CommentInfo(
                1,
                2,
                "test",
                "전남대병원",
                "익명의 구운란",
                "2023-12-24 13:28:12",
                "https://picsum.photos/300",
                true
            ), myUserId = 1, onDeleteClick = { clickedCommentId ->
                Log.d(
                    "답글삭제 클릭: ", "$clickedCommentId clicked!!!"
                )
            }, onRecommentClick = { clickedCommentId ->
                Log.d(
                    "답글달기 클릭: ", "$clickedCommentId clicked!!!"
                )
            })
        }
    }
}

@Composable
fun TimePickerTest(modifier: Modifier = Modifier) {
    val selectedTime = remember { mutableStateOf<LocalTime?>(null) }
    val selectedDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker { time -> selectedTime.value = time }
            selectedTime.value?.let {
                Text(text = "Selected Time: ${selectedTime.value}")
            }

            DateTimePicker { dateTime -> selectedDateTime.value = dateTime }
            selectedDateTime.value?.let {
                Text(text = "Selected Time: ${selectedDateTime.value}")
            }
        }
    }
}

@Composable
fun ToggleTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            val checkedState = remember { mutableStateOf(true) }
            Toggle(checked = checkedState.value, onCheckedChange = { checkedState.value = it })
        }
    }
}

@Composable
fun ProfileButtonTest(modifier: Modifier = Modifier) {
    val myUserId: Long = 1
    val userInfoList = arrayListOf(
        UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남1",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 1
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남2",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 2
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남3",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 3
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남4",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 4
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남5",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 5
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남6",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 6
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남7",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 7
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남8",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 8
        ), UserInfo(
            profileImgUrl = "https://picsum.photos/300",
            userName = "김호남9",
            empNo = "18-12543",
            userEmail = "test@test.com",
            userId = 9
        )
    )
    val selectedList = remember { mutableStateListOf<Long>(0, 0, 0) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            ProfileButtonList(userInfoList, selectedList, myUserId)
        }
    }
}

@Composable
fun CardTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            ProfileCard(
                UserInfo(
                    profileImgUrl = "https://picsum.photos/300",
                    userName = "김호남",
                    empNo = "18-12543",
                    userEmail = "test@test.com",
                    userId = 2
                )
            )
        }
    }
}

@Composable
fun AgreeListTest(modifier: Modifier = Modifier) {
    val (ageChecked, setAgeClick) = remember { mutableStateOf(false) }
    val (agreeChecked, setAgreeClick) = remember { mutableStateOf(false) }
    val (infoChecked, setInfoClick) = remember { mutableStateOf(false) }
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        AgreeList(ageChecked, setAgeClick, agreeChecked, setAgreeClick, infoChecked, setInfoClick)
    }
}

@Composable
fun InputTest(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val pin = remember { mutableStateOf("") }
    Surface(modifier.addFocusCleaner(focusManager), color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            val text1 = remember { mutableStateOf("") }
            val text2 = remember { mutableStateOf("") }
            val text3 = remember { mutableStateOf("") }
            SingleInput(
                text = text1.value,
                onValueChange = { text1.value = it },
                focusManager = focusManager,
                placeholder = "사번 입력",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            MultiInput(
                text = text2.value,
                onValueChange = { text2.value = it },
                focusManager = focusManager,
                placeholder = "사번 입력"
            )
            SearchInput(text = text3.value,
                onValueChange = { text3.value = it },
                focusManager = focusManager,
                placeholder = "근무지 지역 선택",
                onClickDone = { Log.d("click done: ", text3.value) })
            PassInput(pin = pin.value, onValueChange = { pin.value = it })
        }
    }
}

@Composable
fun CheckBoxTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            val (checked1, setChecked1) = remember { mutableStateOf(false) }
            val (checked2, setChecked2) = remember { mutableStateOf(false) }
            val (checked3, setChecked3) = remember { mutableStateOf(false) }
            val allChecked = (checked1 && checked2 && checked3)
            CheckBoxRow(
                text = "전체 동의",
                value = allChecked,
                onClick = {
                    if (allChecked) {
                        setChecked1(false)
                        setChecked2(false)
                        setChecked3(false)
                    } else {
                        setChecked1(true)
                        setChecked2(true)
                        setChecked3(true)
                    }
                },
            )
            CheckBoxRow(text = "동의1", value = checked1, onClick = { setChecked1(!checked1) })
            CheckBoxRow(text = "동의2", value = checked2, onClick = { setChecked2(!checked2) })
            CheckBoxRow(text = "동의3", value = checked3, onClick = { setChecked3(!checked3) })
        }
    }
}

@Composable
fun ButtonTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            BigButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.bodyLarge, text = "회원가입 완료하기"
                )
            }
            MiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Row(Modifier.fillMaxSize(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
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

            ThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.labelLarge, text = "근무표 등록하기"
                )
            }

            HalfBigButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge, text = "취소"
                )
            }

            HalfMiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Gray800,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge, text = "취소"
                )
            }

            HalfThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") }, colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge, text = "취소"
                )
            }

            Row {
                AuthButton(onClick = { Log.d("test: ", "clicked!!!") }, type = "kakao")
                AuthButton(onClick = { Log.d("test: ", "clicked!!!") }, type = "naver")
                AuthButton(onClick = { Log.d("test: ", "clicked!!!") }, type = "google")
            }

            GroupButton(
                onClick = { Log.d("test: ", "clicked!!!") },
                groupMaster = "김다희",
                groupName = "호남향우회",
                memberCnt = 1,
                groupImage = 1,
                groupId = 1
            )

            Row {
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") }, UserInfo(
                        profileImgUrl = "https://picsum.photos/300", userId = 1, userName = "김호남"
                    ), isMine = true, isSelected = true
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") }, UserInfo(
                        profileImgUrl = "https://picsum.photos/300", userId = 1, userName = "김호남"
                    ), isMine = true, isSelected = false
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") }, UserInfo(
                        profileImgUrl = "https://picsum.photos/300", userId = 1, userName = "김호남"
                    ), isMine = false, isSelected = true
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") }, UserInfo(
                        profileImgUrl = "https://picsum.photos/300", userId = 1, userName = "김호남"
                    ), isMine = false, isSelected = false
                )
            }

            SettingButton(
                onClick = { Log.d("test: ", "clicked!!!") },
                text = "내 정보 설정",
                color = NaturalBlack,
                icon = MySetting
            )
            SettingButton(
                onClick = { Log.d("test: ", "clicked!!!") },
                text = "로그아웃",
                color = Error500,
                icon = Logout
            )
            IconTextButton(onClick = {}, width = 70, height = 30, icon = Search, text = "안녕")
        }
    }
}
