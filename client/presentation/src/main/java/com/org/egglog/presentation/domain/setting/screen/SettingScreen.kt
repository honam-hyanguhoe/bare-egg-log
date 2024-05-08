package com.org.egglog.presentation.domain.setting.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.BasicButton
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.listItems.InfoList
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.getVersionInfo

@Composable
fun SettingScreen(
    onNavigateToPrivacyDetailScreen: () -> Unit,
    onNavigateToAgreeDetailScreen: () -> Unit,
    onNavigateToMySettingScreen: () -> Unit
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val version = getVersionInfo(context)

    fun onClickPrepared() {
        openDialog.value = true
    }

    Surface {
        Column (
            modifier = Modifier
                .padding(vertical = 6.widthPercent(context).dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(20.heightPercent(context).dp))
            Text(text = "마이페이지", style = Typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.heightPercent(context).dp))
            // 인증 배지 신청하기 버튼
            BasicButton(modifier = Modifier
                .height(74.heightPercent(context).dp)
                .fillMaxWidth(0.94f), onClick = { onClickPrepared() }, shape = RoundedCornerShape(16.widthPercent(context).dp), colors = ButtonColors(
                containerColor = Warning300,
                contentColor = NaturalWhite,
                disabledContainerColor = Gray300,
                disabledContentColor = NaturalWhite
            )) {
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically,) {
                        LocalImageLoader(imageUrl = R.drawable.small_fry, Modifier.size(32.widthPercent(context).dp))
                        Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
                        Column {
                            Text(text = "인증 배지 신청하기", style = Typography.bodyLarge)
                            Text(text = "면허를 인증할 수 있어요", style = Typography.displayMedium)
                        }
                    }
                    Icon(imageVector = ArrowRight, modifier = Modifier.size(24.widthPercent(context).dp), color = NaturalWhite)
                }
            }
            InfoList(
                onClickPrepared = { onClickPrepared() },
                onNavigateToPrivacyDetailScreen = onNavigateToPrivacyDetailScreen,
                onNavigateToAgreeDetailScreen = onNavigateToAgreeDetailScreen,
                onNavigateToMySettingScreen = onNavigateToMySettingScreen
            )
            
            Spacer(modifier = Modifier.padding(30.heightPercent(context).dp))
            LocalImageLoader(imageUrl = R.drawable.bottom_logo, Modifier.fillMaxWidth(0.6f))
            Text("Version $version", style = Typography.displayMedium.copy(color = Gray400))

            when {
                openDialog.value -> {
                    Dialog(
                        onDismissRequest = { openDialog.value = false },
                        onConfirmation = { openDialog.value = false },
                        dialogTitle = "공지",
                        dialogText = "출시 준비 중입니다",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    ClientTheme {
        SettingScreen(
            onNavigateToPrivacyDetailScreen = {},
            onNavigateToAgreeDetailScreen = {},
            onNavigateToMySettingScreen = {}
        )
    }
}