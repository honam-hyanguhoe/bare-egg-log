package com.org.egglog.client.ui.atoms.wheelPicker.basic

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.wheelPicker.core.DefaultWheelDatePicker
import com.org.egglog.client.ui.atoms.wheelPicker.core.SelectorProperties
import com.org.egglog.client.ui.atoms.wheelPicker.core.WheelPickerDefaults
import java.time.LocalDate

@Composable
fun WheelDatePicker(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    yearsRange: IntRange? = IntRange(1924, 2124),
    size: DpSize = DpSize(256.dp, 128.dp),
    rowCount: Int = 3,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedDate : (snappedDate: LocalDate) -> Unit = {}
) {
    DefaultWheelDatePicker(
        modifier,
        startDate,
        minDate,
        maxDate,
        yearsRange,
        size,
        rowCount,
        textStyle,
        textColor,
        selectorProperties,
        onSnappedDate = { snappedDate ->
            onSnappedDate(snappedDate.snappedLocalDate)
            snappedDate.snappedIndex
        }
    )
}