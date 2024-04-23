package com.org.egglog.client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.cards.ResultCard
import com.org.egglog.client.ui.theme.ClientTheme
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.atoms.imageLoader.UrlImageLoader
import com.org.egglog.client.utils.MessageUtil
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.AddBox
import com.org.egglog.client.utils.Logout
import com.org.egglog.client.utils.MySetting
import com.org.egglog.client.utils.User


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
fun MyApp(modifier: Modifier = Modifier) {
//    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
//
//    Surface(modifier, color = MaterialTheme.colorScheme.background) {
//        if (shouldShowOnboarding) {
//            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
//        } else {
//            Greetings()
//        }
//    }
    ButtonTest()
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.width(320.widthPercent(LocalContext.current).dp),
            onClick = onContinueClicked
        ) {
            Text("${320.widthPercent(LocalContext.current).dp}")
//            Icon(Notification, modifier = Modifier.size(25.dp))
        }
        LocalImageLoader(imageUrl = R.drawable.off)
        ResultCard(message = MessageUtil.APPROVE)
        ResultCard(message = MessageUtil.APPLY)
        ResultCard(message = MessageUtil.REGISTER)
        ResultCard(message = MessageUtil.NO_SEARCH_RESULT)
        Image(painter = painterResource(id = R.drawable.bottom_logo), contentDescription = null)
        UrlImageLoader(imageUrl = "https://picsum.photos/300", modifier = Modifier.size(320.widthPercent(LocalContext.current).dp))
        LocalImageLoader(imageUrl = R.drawable.off)

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
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//    name = "DefaultPreviewLight"
//)
@Composable
fun MyAppPreview() {
    ClientTheme {
        MyApp(Modifier.fillMaxSize())
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

            SettingButton(onClick = {Log.d("test: ", "clicked!!!")}, text =  "내 정보 설정", color = NaturalBlack, icon = MySetting)
            SettingButton(onClick = {Log.d("test: ", "clicked!!!")}, text =  "로그아웃", color = Error500, icon = Logout)
        }
    }
}

@Preview
@Composable
fun ButtonsPreview() {
    ClientTheme {
        ButtonTest(Modifier.fillMaxSize())
    }
}