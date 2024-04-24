package com.org.egglog.client

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.buttons.AuthButton
import com.org.egglog.client.ui.atoms.buttons.BigButton
import com.org.egglog.client.ui.atoms.buttons.GroupButton
import com.org.egglog.client.ui.atoms.buttons.HalfBigButton
import com.org.egglog.client.ui.atoms.buttons.HalfMiddleButton
import com.org.egglog.client.ui.atoms.buttons.HalfThinButton
import com.org.egglog.client.ui.atoms.buttons.MiddleButton
import com.org.egglog.client.ui.atoms.buttons.ProfileButton
import com.org.egglog.client.ui.atoms.buttons.SettingButton
import com.org.egglog.client.ui.atoms.buttons.ThinButton
import com.org.egglog.client.ui.atoms.dialogs.BottomSheet
import com.org.egglog.client.ui.atoms.dialogs.InteractiveBottomSheet
import com.org.egglog.client.ui.atoms.checkbox.CheckBoxRow
import com.org.egglog.client.ui.atoms.dialogs.SheetContent
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.atoms.inputs.MultiInput
import com.org.egglog.client.ui.atoms.inputs.PassInput
import com.org.egglog.client.ui.atoms.inputs.SingleInput
import com.org.egglog.client.ui.theme.ClientTheme
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.atoms.labels.Labels
import com.org.egglog.client.ui.atoms.menus.ScrollableMenus
import com.org.egglog.client.ui.atoms.toggle.Toggle
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.AddBox
import com.org.egglog.client.utils.Favorite
import com.org.egglog.client.utils.Logout
import com.org.egglog.client.utils.MoreVert
import com.org.egglog.client.utils.MySetting
import com.org.egglog.client.utils.addFocusCleaner

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
    var showBottomSheet by remember { mutableStateOf<Boolean>(false) }

    fun dismissSheet() {
        showBottomSheet = false
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomSheet(
            height = 400.dp,
            showBottomSheet = showBottomSheet,
            onDismiss = {
                dismissSheet()
            },
        ) {
            BottomSheetContent()
        };
    }
}

@Composable
fun BottomSheetContent() {
    Text(text = "BottomSheetContent 테스트")
    Spacer(modifier = Modifier.height(180.dp))
}

//@Composable
//fun BottomSheetTest(){
//    BottomSheet()
//}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .size(320.dp)
            .padding(vertical = 4.widthPercent(LocalContext.current).dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    style = Typography.titleLarge,
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "DefaultPreviewDark"
//)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)


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
fun InputTest(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val pin = remember { mutableStateOf("") }
    Surface(modifier.addFocusCleaner(focusManager), color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            val text1 = remember { mutableStateOf("") }
            val text2 = remember { mutableStateOf("") }
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
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.bodyLarge,
                    text = "회원가입 완료하기"
                )
            }
            MiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
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
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.labelLarge,
                    text = "근무표 등록하기"
                )
            }

            HalfBigButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
                )
            }

            HalfMiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Gray800,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
                )
            }

            HalfThinButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Gray25,
                    containerColor = Gray300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Text(
                    style = Typography.displayLarge,
                    text = "취소"
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
                    onClick = { Log.d("test: ", "clicked!!!") },
                    profileImgUrl = "https://picsum.photos/300",
                    isMine = true,
                    isSelected = true,
                    userId = 1,
                    userName = "김호남"
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") },
                    profileImgUrl = "https://picsum.photos/300",
                    isMine = true,
                    isSelected = false,
                    userId = 1,
                    userName = "김호남"
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") },
                    profileImgUrl = "https://picsum.photos/300",
                    isMine = false,
                    isSelected = true,
                    userId = 1,
                    userName = "김호남"
                )
                ProfileButton(
                    onClick = { Log.d("test: ", "clicked!!!") },
                    profileImgUrl = "https://picsum.photos/300",
                    isMine = false,
                    isSelected = false,
                    userId = 1,
                    userName = "김호남"
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
        }
    }
}

