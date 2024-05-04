package com.org.egglog.presentation.domain.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.MultiInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray25
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun PostEditorScreen() {
    val focusManager = LocalFocusManager.current
    Surface {
        BoxWithConstraints {
            val screenHeight = maxHeight
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = NaturalWhite)
                    .padding(bottom = 24.heightPercent(LocalContext.current).dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicHeader(
                        hasLeftClose = true,
                        onClickClose = { },
                    )
                    Column (
                        modifier = Modifier.padding(top = 12.heightPercent(LocalContext.current).dp)
                    ){
                        Text(text = "제목", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        SingleInput(text = "", placeholder = "제목을 입력해주세요", onValueChange = {}, focusManager = focusManager)
                        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))
                        Text(text = "내용", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        MultiInput(text = "", placeholder = "내용을 입력해주세요", onValueChange = {}, focusManager = focusManager, height = 150)
                        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))
                        Text(text = "사진(선택)", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                    }
                }
                BigButton(
                    colors = ButtonColors(
                        contentColor = NaturalWhite,
                        containerColor = Warning300,
                        disabledContainerColor = Gray25,
                        disabledContentColor = Gray300
                    ), onClick = { }) {
                    Text(
                        style = Typography.bodyLarge,
                        text = "작성완료"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PostEditorPreviewScreen(){
    ClientTheme {
        PostEditorScreen()
    }
}