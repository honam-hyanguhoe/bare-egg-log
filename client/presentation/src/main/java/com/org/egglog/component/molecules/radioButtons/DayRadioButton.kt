package com.org.egglog.component.molecules.radioButtons

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.buttons.RadioLabelButton
import com.org.egglog.theme.*

@Composable
fun DayRadioButton(radioList: ArrayList<String>, selected: MutableState<String>) {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth()) {
        radioList.mapIndexed { index, text ->
            RadioLabelButton(
                text = text,
                isSelected = selected.value == text,
                onClick = { clickedIdx ->
                    Log.d("", clickedIdx)
                    selected.value = clickedIdx },
                radioButtonColorInfo = RadioButtonColorInfo(
                    selectedBorderColor = NaturalBlack,
                    selectedContainerColor = NaturalBlack,
                    selectedTextColor = Gray200,
                    unSelectedBorderColor = Gray200,
                    unSelectedContainerColor = Gray200,
                    unSelectedTextColor = NaturalBlack
                ),
                textStyle = Typography.bodyLarge,
                width = 68,
                padding = 6
            )
            if(index != radioList.size - 1) Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
        }
    }
}