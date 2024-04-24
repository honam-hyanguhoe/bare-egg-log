package com.org.egglog.client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.buttons.AuthButton
import com.org.egglog.client.ui.atoms.buttons.BigButton
import com.org.egglog.client.ui.atoms.buttons.FloatingButton
import com.org.egglog.client.ui.atoms.buttons.GroupButton
import com.org.egglog.client.ui.atoms.buttons.HalfBigButton
import com.org.egglog.client.ui.atoms.buttons.HalfMiddleButton
import com.org.egglog.client.ui.atoms.buttons.HalfThinButton
import com.org.egglog.client.ui.atoms.buttons.MiddleButton
import com.org.egglog.client.ui.atoms.buttons.ProfileButton
import com.org.egglog.client.ui.atoms.buttons.SettingButton
import com.org.egglog.client.ui.atoms.buttons.ThinButton
import com.org.egglog.client.ui.atoms.cards.ProfileCard
import com.org.egglog.client.ui.atoms.checkbox.CheckBoxRow
import com.org.egglog.client.ui.atoms.dialogs.BottomSheet
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
import com.org.egglog.client.ui.organisms.agreeList.AgreeList
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.AddBox
import com.org.egglog.client.utils.Logout
import com.org.egglog.client.utils.MySetting
import com.org.egglog.client.utils.addFocusCleaner
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.log


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

@OptIn(ExperimentalMaterial3Api::class)
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
    CardTest()
}

@Composable
fun BottomSheetTest() {
    var showBottomSheet by remember { mutableStateOf<Boolean>(false) }

    fun dismissSheet() {
        showBottomSheet = false
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = "플로팅 버튼 테스트")
        FloatingButton {
            println("플로팅 버튼")
        }
    }
}

@Composable
fun BottomSheetContent() {
    Text(text = "BottomSheetContent 테스트")
    Spacer(modifier = Modifier.height(180.dp))
}


@Composable
fun LabelTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Day",size = "big")
                Labels(text = "Eve",size = "big")
                Labels(text = "Night",size = "big")
                Labels(text = "교육",size = "big")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Eve")
                Labels(text = "Off")
                Labels(text = "Night")
                Labels(text = "Eve")
                Labels(text = "보건")
                Labels(text = "휴가")
                Labels(text= "None")
            }
        }
    }
}

@Composable
fun TimePickerTest(modifier: Modifier = Modifier) {
    val selectedTime = remember { mutableStateOf<LocalTime?>(null) }
    val selectedDateTime = remember { mutableStateOf<LocalDateTime?>(null) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TimePicker { time -> selectedTime.value = time }
            selectedTime.value?.let {
                Text(text = "Selected Time: ${selectedTime.value}")
            }

            DateTimePicker{dateTime -> selectedDateTime.value = dateTime}
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
            Toggle(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
    }
}

@Composable
fun CardTest(modifier: Modifier = Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            ProfileCard(profileImgUrl = "https://picsum.photos/300", userName = "김호남", empNo = "18-12543", userEmail = "test@test.com", userId = 2)
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
            SearchInput(
                text = text3.value,
                onValueChange = { text3.value = it },
                focusManager = focusManager,
                placeholder = "근무지 지역 선택",
                onClickDone = { Log.d("click done: ", text3.value) }
            )
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
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Text(
                    style = Typography.bodyLarge,
                    text = "회원가입 완료하기"
                )
            }
            MiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Row(Modifier.fillMaxSize(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
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

            ThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Text(
                    style = Typography.labelLarge,
                    text = "근무표 등록하기"
                )
            }

            HalfBigButton(
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
                )
            }

            HalfMiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Gray800,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
                )
            }

            HalfThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!")},
                colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
                )
            }

            Row {
                AuthButton(onClick = {Log.d("test: ", "clicked!!!")}, type = "kakao")
                AuthButton(onClick = {Log.d("test: ", "clicked!!!")}, type = "naver")
                AuthButton(onClick = {Log.d("test: ", "clicked!!!")}, type = "google")
            }

            GroupButton(onClick = {Log.d("test: ", "clicked!!!")}, groupMaster = "김다희", groupName = "호남향우회", memberCnt = 1, groupImage = 1, groupId = 1)

            Row {
                ProfileButton(onClick = {Log.d("test: ", "clicked!!!")}, profileImgUrl = "https://picsum.photos/300", isMine = true, isSelected = true, userId = 1, userName = "김호남")
                ProfileButton(onClick = {Log.d("test: ", "clicked!!!")}, profileImgUrl = "https://picsum.photos/300", isMine = true, isSelected = false, userId = 1, userName = "김호남")
                ProfileButton(onClick = {Log.d("test: ", "clicked!!!")}, profileImgUrl = "https://picsum.photos/300", isMine = false, isSelected = true, userId = 1, userName = "김호남")
                ProfileButton(onClick = {Log.d("test: ", "clicked!!!")}, profileImgUrl = "https://picsum.photos/300", isMine = false, isSelected = false, userId = 1, userName = "김호남")
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
        }
    }
}
