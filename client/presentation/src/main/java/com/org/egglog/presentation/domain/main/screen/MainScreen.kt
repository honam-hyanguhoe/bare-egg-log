package com.org.egglog.presentation.domain.main.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.radioButtons.DayRadioButton
import com.org.egglog.presentation.component.organisms.calendars.WeeklyCalendar
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.webView.ContentWebView
import com.org.egglog.presentation.domain.community.posteditor.activity.PostEditorActivity
import com.org.egglog.presentation.domain.community.posteditor.screen.WritePostPreviewScreen
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.PostSideEffect
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.main.viewModel.MainSideEffect
import com.org.egglog.presentation.domain.main.viewModel.MainViewModel
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.ArrowLeft
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value

    MainScreen(
        onClickBefore = viewModel::onClickBefore,
        onClickAfter = viewModel::onClickAfter
    )
}


@Composable
private fun MainScreen(
    onClickBefore : () -> Unit,
    onClickAfter : () -> Unit
) {
//    val selectedItem = remember { mutableStateOf(2) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
//    Surface {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Box(modifier = Modifier.weight(1f)) {
//                when (selectedItem) {
////                    0 -> CalendarPage()
////                    1 -> GroupPage()
////                    2 -> HomePage()
////                    3 -> CommunityPage()
////                    4 -> SettingsPage()
//                }
//            }
//            BottomNavigator(selectedItem = selectedItem, onItemSelected = { onItemSelected })
//        }
//    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NaturalWhite)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .width(340.widthPercent(context).dp)
                .height(185.heightPercent(context).dp)
                .border(
                    1.dp, NaturalBlack,
                    RoundedCornerShape(15.dp)
                )
        ){
            Text(text = "으아. 집 갈래요", style = Typography.displayLarge)
        }
        Spacer(modifier = Modifier.height(30.dp))
        DutyCard( onClickBefore = {}, onClickAfter= {})
        Spacer(modifier = Modifier.height(30.dp))
        RemainCard()
        Spacer(modifier = Modifier.height(30.dp))
        StaticsCard()
    }
}

@Composable
fun DutyCard(
    onClickBefore : () -> Unit,
    onClickAfter : () -> Unit
) {
    val tempLabels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")
    val dataSource = WeeklyDataSource()
    var calendarUiModel2 by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "내 근무 일정",
                modifier = Modifier,
                style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Row(
                modifier = Modifier
            ){
                CustomIconButton(size = 30.dp, imageVector = ArrowLeft, color = NaturalBlack, onClick = { /*TODO*/ })
                CustomIconButton(size = 30.dp, imageVector = ArrowRight, color = NaturalBlack, onClick = { /*TODO*/ })
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        WeeklyCalendar(
            type = "main",
            calendarUiModel = calendarUiModel2,
            labels = tempLabels,
            width = 320,
            backgroundColor = Gray100
        )
    }
}

@Composable
fun RemainCard() {
    val context = LocalContext.current
    // radio
    val radioList = arrayListOf("Week", "Month")
    val selected = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        Text(
            text = "제 근무는 언제 끝나죠?",
            modifier = Modifier.padding(start = 10.dp),
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)) {
            DayRadioButton(radioList = radioList, selected = selected)
        }
        ContentWebView(width = 320, height = 350, url = "https://www.egg-log.org/remain")
    }
}

@Composable
fun StaticsCard() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        Text(
            text = "내 근무 통계",
            modifier = Modifier.padding(start = 10.dp),
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        ContentWebView(width = 320, height = 350, url = "https://www.egg-log.org/statics")
    }
}

@Preview
@Composable
fun MainScreenPreview() {
//    MainScreen()
}


