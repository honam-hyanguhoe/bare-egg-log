package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.theme.Typography

@Composable
fun RadioLabelButton(padding: Int, width: Int, text: String, isSelected: Boolean, onClick: (String) -> Unit, radioButtonColorInfo: RadioButtonColorInfo, textStyle: TextStyle = Typography.bodyMedium) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.width(width.widthPercent(context).dp),
        shape = RoundedCornerShape(50.widthPercent(context).dp),
        border = BorderStroke(color = if(isSelected) radioButtonColorInfo.selectedBorderColor else radioButtonColorInfo.unSelectedBorderColor, width = 1.widthPercent(context).dp),
        onClick = { onClick(text) },
        contentColor = if(isSelected) radioButtonColorInfo.selectedContainerColor else radioButtonColorInfo.unSelectedContainerColor,
        color = if(isSelected) radioButtonColorInfo.selectedContainerColor else radioButtonColorInfo.unSelectedContainerColor
    ) {
        Box(Modifier.padding(vertical = padding.widthPercent(context).dp), contentAlignment = Alignment.Center) {
            Text(text = text, color = if(isSelected) radioButtonColorInfo.selectedTextColor else radioButtonColorInfo.unSelectedTextColor, style = textStyle)
        }
    }
}