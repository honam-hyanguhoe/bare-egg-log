package com.org.egglog.presentation.component.molecules.radioButtons

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.RadioLabelButton
import com.org.egglog.presentation.theme.*

@Composable
fun DayRadioButton(
    radioList: ArrayList<String>, selected: MutableState<String>
) {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth()) {
        radioList.mapIndexed { index, text ->
            RadioLabelButton(
                text = text,
                isSelected = selected.value == text,
                onClick = { clickedIdx ->
                    Log.d("", clickedIdx)
                    selected.value = clickedIdx
                },
                radioButtonColorInfo = RadioButtonColorInfo(
                    selectedBorderColor = NaturalBlack,
                    selectedContainerColor = NaturalBlack,
                    selectedTextColor = Gray100,
                    unSelectedBorderColor = NaturalBlack,
                    unSelectedContainerColor = Gray100,
                    unSelectedTextColor = NaturalBlack
                ),
                textStyle = Typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                width = 64,
                padding = 8
            )
            if (index != radioList.size - 1) Spacer(
                modifier = Modifier.padding(
                    3.widthPercent(
                        context
                    ).dp
                )
            )
        }
    }
}