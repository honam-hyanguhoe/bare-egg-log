package com.org.egglog.presentation.component.organisms.workSetting

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.buttons.BasicButton
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.atoms.wheelPicker.TimePicker
import com.org.egglog.presentation.component.molecules.dialogButton.DialogButton
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalTime

@Composable
fun AlarmModifySheet(
    onAlarmTimeChange: (LocalTime) -> Unit,
    onReplayCntChange: (Int) -> Unit,
    onReplayTimeChange: (Int) -> Unit,
    addEnabled: Boolean,
    onClickAdd: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.widthPercent(context).dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(10.widthPercent(context).dp)
                    .background(Color.Transparent, CircleShape)
                    .border(3.dp, Primary600, CircleShape)) {}
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "시간", style = Typography.bodyLarge)
        }
        TimePicker(onTimeSelected = onAlarmTimeChange)

        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        Text(text = "간격", style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        DialogButton(onChange = onReplayTimeChange, interval = 5, nextText = "분")

        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        Text(text = "반복 횟수", style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        DialogButton(onChange = onReplayCntChange, interval = 1, nextText = "회")

        Spacer(modifier = Modifier.weight(1f))
        BigButton(colors = ButtonColors(
            containerColor = Warning300,
            contentColor = NaturalWhite,
            disabledContentColor = NaturalWhite,
            disabledContainerColor = Gray300
        ), enabled = addEnabled, onClick = { onClickAdd() }) {
            Text(text = "추가하기", color = NaturalWhite)
        }
    }
}