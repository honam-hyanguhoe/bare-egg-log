package com.org.egglog.client.ui.molecules.radioButtons

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.RadioLabelButton
import com.org.egglog.presentation.theme.*

@Composable
fun WorkRadioButton(radioList: ArrayList<String>, selected: MutableState<String>) {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
        radioList.mapIndexed { index, text ->
            RadioLabelButton(
                text = text,
                isSelected = selected.value == text,
                onClick = { clickedIdx ->
                    Log.d("", clickedIdx)
                    selected.value = clickedIdx
                },
                radioButtonColorInfo = RadioButtonColorInfo(
                    selectedBorderColor = Gray500,
                    selectedContainerColor = Gray500,
                    selectedTextColor = NaturalWhite,
                    unSelectedBorderColor = Gray300,
                    unSelectedContainerColor = White,
                    unSelectedTextColor = Gray300
                ),
                width = 52,
                padding = 2,
                textStyle = Typography.bodyMedium
            )
            if (index != radioList.size - 1) Spacer(
                modifier = Modifier.padding(
                    2.widthPercent(
                        context
                    ).dp
                )
            )
        }
    }
}
