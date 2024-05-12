package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.presentation.component.atoms.cards.BannerCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.tabBar.TabBar
import com.org.egglog.presentation.component.organisms.workSetting.AlarmManager
import com.org.egglog.presentation.component.organisms.workSetting.WorkManager
import com.org.egglog.presentation.domain.setting.viewmodel.WorkSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.WorkSettingViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.heightPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun WorkSettingScreen(
    viewModel: WorkSettingViewModel = hiltViewModel(),
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is WorkSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    WorkSettingScreen(
        workTypeList = state.workTypeList,
        getWorkListInit = viewModel::getWorkListInit
    )
}

@Composable
fun WorkSettingScreen(
    workTypeList: List<WorkType>,
    getWorkListInit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(true) {
        getWorkListInit()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
            .systemBarsPadding()
            .addFocusCleaner(focusManager)
    ) {
        BasicHeader(
            title = "근무 설정",
            hasTitle = true,
            hasArrow = true,
            hasProgressBar = false,
            onClickBack = { (context as? Activity)?.onBackPressed() },
            onClickLink = { },
            onClickMenus = { },
            selectedOption = null
        )
        Spacer(modifier = Modifier.padding(6.heightPercent(context).dp))
        BannerCard()
        TabBar(titles = listOf("근무", "알람"), firstContent = { WorkManager(workTypeList = workTypeList) }, secendContent = { AlarmManager() })
    }
}

@Preview
@Composable
private fun MySettingScreenPreview() {
    ClientTheme {
        MySettingScreen()
    }
}