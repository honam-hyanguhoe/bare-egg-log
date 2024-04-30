package com.org.egglog.component.atoms.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.theme.*
import com.org.egglog.utils.widthPercent

@Composable
fun PassInput(pin: String, onValueChange: (String) -> Unit) {
    val context = LocalContext.current

    ComposePinInput(
        value = pin,
        cellBorderWidth = 2.widthPercent(context).dp,
        cellBorderColor = Warning300,
        focusedCellBorderColor = Warning400,
        onValueChange = onValueChange,
        cellSize = 40.widthPercent(context).dp,
        maxSize = 6,
        style = ComposePinInputStyle.BOX,
        fontColor = NaturalBlack
    )
}