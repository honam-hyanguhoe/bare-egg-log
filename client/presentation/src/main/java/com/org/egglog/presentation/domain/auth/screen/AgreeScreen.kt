package com.org.egglog.presentation.domain.auth.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.agreeList.AgreeList
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.heightPercent

@SuppressLint("RememberReturnType")
@Composable
fun AgreeScreen() {
    Surface {
        Column {
            BasicHeader(
                title = "약관동의",
                hasTitle = true,
                hasArrow = true,
                hasClose = true,
                hasProgressBar = true,
                onClickBack = { /*TODO*/ },
                onClickLink = { /*TODO*/ },
                onClickClose = { /*TODO*/ },
                onClickMenus = { /*TODO*/ },
                selectedOption = null
            )
            Spacer(modifier = Modifier.height(100.heightPercent(LocalContext.current).dp))
            Text(text = "이용 약관에 대한 동의가 필요해요", style = Typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.heightPercent(LocalContext.current).dp))
            val (ageChecked, setAgeClick) = remember { mutableStateOf(false) }
            val (agreeChecked, setAgreeClick) = remember { mutableStateOf(false) }
            val (infoChecked, setInfoClick) = remember { mutableStateOf(false) }
            AgreeList(ageChecked, setAgeClick, agreeChecked, setAgreeClick, infoChecked, setInfoClick)
        }
    }
}

@Preview
@Composable
fun AgreeScreenPreview() {
    ClientTheme {
        AgreeScreen()
    }
}