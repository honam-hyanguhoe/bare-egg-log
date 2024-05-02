package com.org.egglog.presentation.domain.auth.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.client.data.UserInfo
import com.org.egglog.client.ui.molecules.profileButtonList.ProfileButtonList
import com.org.egglog.client.ui.molecules.radioButtons.WorkRadioButton
import com.org.egglog.presentation.utils.AddBox
import com.org.egglog.presentation.utils.Logout
import com.org.egglog.presentation.utils.MySetting
import com.org.egglog.presentation.utils.Search
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.AuthButton
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.buttons.FloatingButton
import com.org.egglog.presentation.component.atoms.buttons.GroupButton
import com.org.egglog.presentation.component.atoms.buttons.HalfBigButton
import com.org.egglog.presentation.component.atoms.buttons.HalfMiddleButton
import com.org.egglog.presentation.component.atoms.buttons.HalfThinButton
import com.org.egglog.presentation.component.atoms.buttons.IconTextButton
import com.org.egglog.presentation.component.atoms.buttons.MiddleButton
import com.org.egglog.presentation.component.atoms.buttons.ProfileButton
import com.org.egglog.presentation.component.atoms.buttons.SettingButton
import com.org.egglog.presentation.component.atoms.buttons.ThinButton
import com.org.egglog.presentation.component.atoms.cards.ProfileCard
import com.org.egglog.presentation.component.atoms.checkbox.CheckBoxRow
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.inputs.MultiInput
import com.org.egglog.presentation.component.atoms.inputs.PassInput
import com.org.egglog.presentation.component.atoms.inputs.SearchInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.atoms.labels.Labels
import com.org.egglog.presentation.component.atoms.profileItem.ProfileItem
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.component.atoms.wheelPicker.DateTimePicker
import com.org.egglog.presentation.component.atoms.wheelPicker.TimePicker
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.cards.AlarmScheduleCard
import com.org.egglog.presentation.component.molecules.cards.AlarmSettingCard
import com.org.egglog.presentation.component.molecules.cards.BigScheduleCard
import com.org.egglog.presentation.component.molecules.cards.CommentCard
import com.org.egglog.presentation.component.molecules.cards.ExcelCard
import com.org.egglog.presentation.component.molecules.cards.HotPostCard
import com.org.egglog.presentation.component.molecules.cards.PostInfo
import com.org.egglog.presentation.component.molecules.cards.SmallScheduleCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.component.molecules.headers.SearchHeader
import com.org.egglog.presentation.component.molecules.listItems.AlarmListItem
import com.org.egglog.presentation.component.molecules.listItems.InfoList
import com.org.egglog.presentation.component.molecules.postReaction.PostReaction
import com.org.egglog.presentation.component.molecules.radioButtons.DayRadioButton
import com.org.egglog.presentation.component.molecules.swiper.Swiper
import com.org.egglog.presentation.component.molecules.tabBar.TabBar
import com.org.egglog.presentation.component.organisms.agreeList.AgreeList
import com.org.egglog.presentation.component.organisms.calendars.GroupCalenar
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.component.organisms.webView.FullPageWebView
import com.org.egglog.presentation.theme.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

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
//    RadioButtonTest()
//    HeaderTest()
//    ListTest()
//    PostCardTest()
//    NavigatorTest()
//    WebViewTest()
    CalendarTest()
}

@Composable
fun WebViewTest() {
//    ContentWebView()
    FullPageWebView(url = "https://www.egg-log.org/remain")
}

@Composable
fun NavigatorTest() {
    var selectedItem by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedItem) {
                0 -> CalendarPage()
                1 -> GroupPage()
                2 -> HomePage()
                3 -> CommunityPage()
                4 -> SettingsPage()
            }
        }
        BottomNavigator(selectedItem = selectedItem, onItemSelected = { selectedItem = it })


    }
}

@Composable
fun CalendarPage() {
    Column() {
        Text("Calendar Page")
        CalendarTest()
    }
}

@Composable
fun GroupPage() {
    Text("Group Page")
}

@Composable
fun HomePage() {
    Text("Home Page")
}

@Composable
fun CommunityPage() {
    Text("Community Page")
}

@Composable
fun SettingsPage() {
    Text("Settings Page")
}

@Composable
fun ListTest() {
    val checkedState = remember { mutableStateOf(false) }
    AlarmListItem(title = "전체 알림",
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it }
    )
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
            onSelect = { selectedMenuItem = it }
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
            onSelect = { selectedMenuItem = it }
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
fun RadioButtonTest() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            val radioList = arrayListOf("eve", "night", "day")
            val selected = remember { mutableStateOf("") }
            WorkRadioButton(radioList = radioList, selected = selected)
            DayRadioButton(radioList = radioList, selected = selected)
        }
    }
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
            CommentCard(
                CommentInfo(
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
            CommentCard(
                CommentInfo(
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


@Composable
fun SwiperTest() {
    fun onDelete() {
        // 삭제버튼 클릭시 실행할 함수
        println("삭제함")
    }

    fun onChnageLeader() {
        // 모임장 위임 버튼 클릭시 실행할 함수
        println("모임장 바꿈")
    }

    Swiper(onDelete = ::onDelete, onChangeLeader = ::onChnageLeader) {
        // swipe 되는 Box 안에 들어갈 요소
        val profile = Profile(1, "김싸피", "전남대학교병원")
        ProfileItem(profile = profile, type = "basic")
    }
}


@Composable
fun CardTest() {
    val onClickPost: () -> Unit = {
        println("클릭됨")
    }

    val onClickMore: (planId: Any) -> Unit = { planId ->
        println("${planId}번 클릭됨")
    }

    val postInfo = PostInfo("부서 골라주실 분!!!", "익명의 구운란", 5, 100, false)
    var checked by remember { mutableStateOf(false) }

    val setToggle: () -> Unit = {
        checked = !checked
    }

    // onClickCard 함수 정의
    val onClickCard: () -> Unit = {
        // Card를 클릭했을 때 수행할 작업
        println("안녕")
    }

    LazyColumn(Modifier.padding(10.dp)) {
        item {
            HotPostCard(postInfo = postInfo, onClickPost = onClickPost)
        }
        item {
            SmallScheduleCard("day", "14:00", "20:00") {
                onClickMore(0)
            }
        }

        item {
            BigScheduleCard("day", "14:00", "20:00", "조선대병원 3중환자실") {
                onClickMore(0)
            }
        }
        item {
            BigScheduleCard("eve", "14:00", "20:00", "조선대병원 3중환자실") {
                onClickMore(0)
            }
        }
        item {
            BigScheduleCard(
                "basic",
                "14:00",
                "20:00",
                "조선대병원 3중환자실",
                title = "추가 근무",
                color = Color(0xFFFDA29B)
            ) {
                onClickMore(0)
            }
        }
        item {
            AlarmScheduleCard(title = "기상 알람", time = "11:00", duration = 30, interval = 5)
        }
        item {
            AlarmSettingCard(
                "Day",
                "14:00",
                30,
                5,
                checked,
                setToggle = setToggle,
                onClickCard = onClickCard
            )
        }

        item {
            AlarmSettingCard(
                "Eve",
                "14:00",
                30,
                5,
                checked,
                setToggle = setToggle,
                onClickCard = onClickCard
            )
        }

        item {
            AlarmSettingCard(
                "개인",
                "14:00",
                30,
                5,
                checked,
                setToggle = setToggle,
                onClickCard = onClickCard,
                color = Color(0xFFFDA29B)
            )
        }


        item {
            Row() {
                ExcelCard(
                    color = "green",
                    date = "2024-03-03",
                    name = "김싸피",
                    onClickCard = { println("안녕하세요") })
                ExcelCard(
                    color = "white",
                    date = "2024-03-03",
                    name = "김싸피",
                    onClickCard = { println("안녕하세요") })
            }
        }
    }
}

@Composable
fun PostCardTest() {
    val profile1 = Profile(1, "익명의 구운란1", "전남대병원", true)
    val postInfo = com.org.egglog.client.data.PostInfo(
        "태화루에 있는 동강병원 어떤가요?",
        "근무환경이나 일의 강도 복지 궁금합니다",
        "https://picsum.photos/300"
    )
    val postInfo2 =
        com.org.egglog.client.data.PostInfo("태화루에 있는 동강병원 어떤가요?", "근무환경이나 일의 강도 복지 궁금합니다")
    val postReaction1 = PostReactionInfo(1, 100, 13, 123, true, true, true)
    LazyColumn(Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
        item { PostCard(profile1, postInfo, postReaction1, onClick = { println("안녕") }) }
        item { PostCard(profile1, postInfo2, postReaction1) }
    }

}

@Composable
fun CalendarTest() {
    Box(
        Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column {
            Text(text = "[ monthly calendar ]")

            Spacer(modifier = Modifier.height(20.dp))

            var currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
            var currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
            var selectedDate =  remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) } // 선택된 날짜를 저장하는 상태

            val onDateClicked: (Int) -> Unit = { clickedDate ->
                // 클릭된 날짜 처리하는 로직을 여기에 작성
                selectedDate.value = clickedDate
                //println("선택한 날짜는 ${clickedDate}")
            }

            val onPrevMonthClick = {
                if (currentMonth.value == 1) {
                    currentMonth.value = 12
                    currentYear.value--
                } else {
                    currentMonth.value--
                }
            }

            val onNextMonthClick = {
                if (currentMonth.value == 12) {
                    currentMonth.value = 1
                    currentYear.value++
                } else {
                    currentMonth.value++
                }
            }
//            MonthlyCalendar(currentYear, currentMonth, onDateClicked, onPrevMonthClick, onNextMonthClick)

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "[ Group Calendar ]")

            Spacer(modifier = Modifier.height(10.dp))

            val myWorkList = mapOf(
                "김다희" to mapOf(
                    "2024-04-01" to "Day",
                    "2024-04-02" to "Eve",
                    "2024-04-03" to "Off",
                    "2024-04-04" to "None"
                )
            )
            val groupWorkList = mapOf(
                "김형민" to mapOf(
                    "2024-04-01" to "Night",
                    "2024-04-02" to "Day",
                    "2024-04-03" to "휴가",
                    "2024-04-04" to "None"
                ),
                "김아현" to mapOf(
                    "2024-04-01" to "Night",
                    "2024-04-02" to "Eve",
                    "2024-04-03" to "보건",
                    "2024-04-04" to "None"
                )
            )
            val workList = myWorkList + groupWorkList
            GroupCalenar(currentYear = currentYear, currentMonth = currentMonth, workList)
        }
    }
}