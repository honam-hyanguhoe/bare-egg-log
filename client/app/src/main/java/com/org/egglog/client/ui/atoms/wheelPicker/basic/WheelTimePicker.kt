package com.org.egglog.client.ui.atoms.wheelPicker.basic

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.wheelPicker.core.DefaultWheelTimePicker
import com.org.egglog.client.ui.atoms.wheelPicker.core.SelectorProperties
import com.org.egglog.client.ui.atoms.wheelPicker.core.TimeFormat
import com.org.egglog.client.ui.atoms.wheelPicker.core.WheelPickerDefaults
import java.time.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    size: DpSize = DpSize(128.dp, 128.dp),
    rowCount: Int = 3,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedTime : (snappedTime: LocalTime) -> Unit = {},
) {
    DefaultWheelTimePicker(
        modifier,
        startTime,
        minTime,
        maxTime,
        timeFormat,
        size,
        rowCount,
        textStyle,
        textColor,
        selectorProperties,
        onSnappedTime = { snappedTime, _ ->
            onSnappedTime(snappedTime.snappedLocalTime)
            snappedTime.snappedIndex
        }
    )
}