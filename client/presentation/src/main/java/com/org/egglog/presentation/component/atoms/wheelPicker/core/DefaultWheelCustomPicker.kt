package com.org.egglog.presentation.component.atoms.wheelPicker.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalDate

@Composable
internal fun DefaultWheelCustomPicker(
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
    onSnappedDate : (snappedDate: SnappedDate) -> Int? = { _ -> null }
) {
    var snappedDate by remember { mutableStateOf(startDate) }

    val months = (1..12).map {
        Month(
            text = if(it < 10) "0${it}" else "$it",
            value = it,
            index = it - 1
        )
    }

    val years = yearsRange?.map {
        Year(
            text = it.toString(),
            value = it,
            index = yearsRange.indexOf(it)
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center){
        if(selectorProperties.enabled().value){
            Surface(
                modifier = Modifier
                    .size(size.width, size.height / rowCount),
                shape = selectorProperties.shape().value,
                color = selectorProperties.color().value,
                border = selectorProperties.border().value
            ) {}
        }
        Row {
            //Year
            years?.let { years ->
                WheelTextPicker(
                    size = DpSize(
                        width = 60.widthPercent(LocalContext.current).dp,
                        height = size.height
                    ),
                    texts = years.map { it.text + "년" },
                    rowCount = rowCount,
                    style = textStyle,
                    color = textColor,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = false
                    ),
                    startIndex = years.find { it.value == startDate.year }?.index ?:0,
                    onScrollFinished = { snappedIndex ->

                        val newYear = years.find { it.index == snappedIndex }?.value

                        newYear?.let {

                            val newDate = snappedDate.withYear(newYear)

                            if(!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                                snappedDate = newDate
                            }

                            val newIndex =  years.find { it.value == snappedDate.year }?.index

                            newIndex?.let {
                                onSnappedDate(
                                    SnappedDate.Year(
                                        localDate = snappedDate,
                                        index = newIndex
                                    )
                                )?.let { return@WheelTextPicker it }

                            }
                        }

                        return@WheelTextPicker years.find { it.value == snappedDate.year }?.index
                    }
                )
            }
            //Month
            WheelTextPicker(
                size = DpSize(
                    width = 42.widthPercent(LocalContext.current).dp,
                    height = size.height
                ),
                texts = months.map { it.text + "월" },
                rowCount = rowCount,
                style = textStyle,
                color = textColor,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = false
                ),
                startIndex = months.find { it.value== startDate.monthValue }?.index ?: 0,
                onScrollFinished = { snappedIndex ->

                    val newMonth = months.find { it.index == snappedIndex }?.value

                    newMonth?.let {

                        val newDate = snappedDate.withMonth(newMonth)

                        if(!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                            snappedDate = newDate
                        }

                        val newIndex =  months.find { it.value == snappedDate.monthValue }?.index

                        newIndex?.let {
                            onSnappedDate(
                                SnappedDate.Month(
                                    localDate = snappedDate,
                                    index = newIndex
                                )
                            )?.let { return@WheelTextPicker it }
                        }
                    }


                    return@WheelTextPicker months.find { it.value == snappedDate.monthValue }?.index
                }
            )
        }
    }
}