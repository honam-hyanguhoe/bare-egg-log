package com.org.egglog.client.ui.atoms.inputs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.widthPercent

@Composable
fun PassInput() {
    var pin by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Box style with dynamic background and font colors
    ComposePinInput(
        value = pin,
        cellBorderWidth = 2.widthPercent(context).dp,
        cellBorderColor = Gray300,
        focusedCellBorderColor = Warning300,
        onValueChange = {
            pin = it
        },
        cellSize = 50.widthPercent(context).dp,
        onPinEntered = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        },
        maxSize = 5,
        style = ComposePinInputStyle.BOX
    )
}