package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.imePadding
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.MultiInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.setting.viewmodel.AskSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.AskSettingViewModel
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AskSettingScreen(
    viewModel: AskSettingViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AskSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    AskSettingScreen(
        title = state.title,
        onTitleChange = viewModel::onTitleChange,
        email = state.email,
        placeholderEmail = state.placeholderEmail,
        onEmailChange = viewModel::onEmailChange,
        content = state.content,
        onContentChange = viewModel::onContentChange,
        onClickSend = viewModel::onClickSend,
        sendEnabled = state.sendEnabled
    )
}

@Composable
fun AskSettingScreen(
    title: String,
    onTitleChange: (String) -> Unit,
    email: String,
    placeholderEmail: String,
    onEmailChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    onClickSend: () -> Unit,
    sendEnabled: Boolean
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .addFocusCleaner(focusManager),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicHeader(
                title = "문의하기",
                hasTitle = true,
                hasArrow = true,
                hasProgressBar = true,
                onClickBack = { (context as? Activity)?.onBackPressed() },
                onClickLink = { },
                onClickMenus = { },
                selectedOption = null
            )

            Column(
                Modifier
                    .padding(horizontal = 10.widthPercent(context).dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(30.heightPercent(LocalContext.current).dp))
                Text(text = "이메일", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(2.heightPercent(LocalContext.current).dp))
                Text(text = "* 미기입 시 가입 이메일로 발송됩니다", style = Typography.displayMedium, color = Gray300)
                Spacer(modifier = Modifier.height(4.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = email,
                    onValueChange = onEmailChange,
                    focusManager = focusManager,
                    placeholder = placeholderEmail,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(18.heightPercent(LocalContext.current).dp))
                Text(text = "제목", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    onValueChange = onTitleChange,
                    focusManager = focusManager,
                    placeholder = "title",
                )

                Spacer(modifier = Modifier.height(18.heightPercent(LocalContext.current).dp))
                Text(text = "내용", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.heightPercent(LocalContext.current).dp))
                MultiInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = content,
                    onValueChange = onContentChange,
                    focusManager = focusManager,
                    placeholder = "content",
                )
                Spacer(modifier = Modifier.weight(1f))
                BigButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.widthPercent(context).dp),
                    colors = ButtonColors(containerColor = Warning300, contentColor = NaturalWhite, disabledContainerColor = Gray300, disabledContentColor = NaturalWhite),
                    onClick = {
                        onClickSend()
                        (context as? Activity)?.onBackPressed()
                        Toast.makeText(context, "문의가 완료되었습니다.", Toast.LENGTH_SHORT).show() },
                    enabled = sendEnabled && title.isNotEmpty() && content.isNotEmpty()
                ) {
                    Text(text = "문의하기", style = Typography.bodyLarge, color = NaturalWhite)
                }
            }
        }
    }
}