package com.org.egglog.presentation.component.organisms.workSetting

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
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalTime

@Composable
fun WorkCreateSheet(
    title: String,
    onTitleChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    placeholder: String = "제목 입력",
    color: String,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
    addEnabled: Boolean,
    onClickAdd: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.widthPercent(context).dp)
            .padding(bottom = 62.dp)
    ) {
        Text(text = "제목", style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        SingleInput(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            onValueChange = onTitleChange,
            focusManager = focusManager,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
        )
        
        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        Text(text = "색상", style = Typography.bodyLarge)
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly,
            Alignment.CenterVertically
        ) {
            BasicButton(modifier = Modifier.size(40.widthPercent(context).dp), enabled = color != "#FEB273", onClick = { onColorChange("#FEB273") }, shape = RoundedCornerShape(50.dp), colors = ButtonColors(
                contentColor = Orange300,
                containerColor = Orange300,
                disabledContainerColor = Orange400,
                disabledContentColor = Orange400
            )) {}
            BasicButton(modifier = Modifier.size(40.widthPercent(context).dp), enabled = color != "#FDA29B", onClick = { onColorChange("#FDA29B") }, shape = RoundedCornerShape(50.dp), colors = ButtonColors(
                contentColor = Error300,
                containerColor = Error300,
                disabledContainerColor = Error400,
                disabledContentColor = Error400
            )) {}
            BasicButton(modifier = Modifier.size(40.widthPercent(context).dp), enabled = color != "#A6F4C5", onClick = { onColorChange("#A6F4C5") }, shape = RoundedCornerShape(50.dp), colors = ButtonColors(
                contentColor = Success200,
                containerColor = Success200,
                disabledContainerColor = Success300,
                disabledContentColor = Success300
            )) {}
            BasicButton(modifier = Modifier.size(40.widthPercent(context).dp), enabled = color != "#53B1FD", onClick = { onColorChange("#53B1FD") }, shape = RoundedCornerShape(50.dp), colors = ButtonColors(
                contentColor = Blue500,
                containerColor = Blue500,
                disabledContainerColor = Blue600,
                disabledContentColor = Blue600
            )) {}
        }

        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(10.widthPercent(context).dp)
                    .background(Color.Transparent, CircleShape)
                    .border(3.dp, Primary600, CircleShape)) {}
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "시작 시간", style = Typography.bodyLarge)
        }
        TimePicker(onTimeSelected = onStartTimeChange)

        Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(10.widthPercent(context).dp)
                    .background(Color.Transparent, CircleShape)
                    .border(3.dp, Primary600, CircleShape)) {}
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "종료 시간", style = Typography.bodyLarge)
        }
        TimePicker(onTimeSelected = onEndTimeChange)

        Spacer(modifier = Modifier.weight(1f))
        BigButton(colors = ButtonColors(
            containerColor = Warning300,
            contentColor = NaturalWhite,
            disabledContentColor = Gray300,
            disabledContainerColor = NaturalWhite
        ), enabled = addEnabled && color.isNotEmpty() && title.isNotEmpty(), onClick = { onClickAdd() }) {
            Text(text = "추가하기", color = NaturalWhite)
        }
    }
}