package com.org.egglog.client.ui.atoms.wheelPicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.wheelPicker.basic.WheelTimePicker
import com.org.egglog.client.ui.atoms.wheelPicker.core.TimeFormat
import com.org.egglog.client.ui.atoms.wheelPicker.core.WheelPickerDefaults
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent
import java.time.LocalTime

@Composable
fun TimePicker(onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    WheelTimePicker(
        timeFormat = TimeFormat.AM_PM,
        selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = RoundedCornerShape(10.widthPercent(context).dp),
            color = Gray200,
            border = BorderStroke(1.widthPercent(context).dp, Gray200)
        ),
        size = DpSize(320.widthPercent(context).dp, 120.heightPercent(context).dp),
        textStyle = Typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
    ) {
        snappedTime -> onTimeSelected(snappedTime)
    }
}