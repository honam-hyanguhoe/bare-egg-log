package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.setting.viewmodel.CalendarSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.CalendarSettingViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CalendarAddScreen(
    viewModel: CalendarSettingViewModel = hiltViewModel(),
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CalendarSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    CalendarAddScreen(
        url = state.url,
        alias = state.alias,
        onUrlChange = viewModel::onUrlChange,
        onAliasChange = viewModel::onAliasChange,
        addEnabled = state.addEnabled,
        onClickAdd = viewModel::onClickAdd

    )
}

@Composable
fun CalendarAddScreen(
    url: String,
    alias: String,
    onUrlChange: (String) -> Unit,
    onAliasChange: (String) -> Unit,
    addEnabled: Boolean,
    onClickAdd: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .addFocusCleaner(focusManager)
        ) {
            BasicHeader(
                title = "캘린더 추가",
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
                    .padding(horizontal = 8.widthPercent(context).dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                Text(text = "구독 URL", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = url,
                    onValueChange = onUrlChange,
                    focusManager = focusManager,
                    placeholder = "ex) example.com/cal.ics",
                )

                Spacer(modifier = Modifier.height(30.heightPercent(LocalContext.current).dp))
                Text(text = "캘린더 이름", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = alias,
                    onValueChange = onAliasChange,
                    focusManager = focusManager,
                    placeholder = "Calendar Name(Alias)",
                )
                Spacer(modifier = Modifier.weight(1f))
                BigButton(
                    enabled = addEnabled && alias.isNotEmpty(),
                    modifier = Modifier.padding(bottom = 6.dp),
                    colors = ButtonColors(
                        containerColor = Warning300,
                        contentColor = NaturalWhite,
                        disabledContainerColor = Gray300,
                        disabledContentColor = NaturalWhite
                    ),
                    onClick = {
                        onClickAdd()
                        focusManager.clearFocus()
                    }
                ) {
                    Text(text = "추가", color = NaturalWhite)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MySettingScreenPreview() {
    ClientTheme {
        MySettingScreen()
    }
}