package com.org.egglog.presentation.component.atoms.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.org.egglog.presentation.theme.*

@Composable
fun CheckBoxRow(text: String = "", value: Boolean, onClick: (Any) -> Unit, enabled: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = value,
            onCheckedChange = onClick,
            colors = CheckboxColors(
                checkedBorderColor = Warning500,
                checkedBoxColor = Warning500,
                checkedCheckmarkColor = MaterialTheme.colorScheme.background,
                disabledCheckedBoxColor = Gray500,
                disabledBorderColor = Gray600,
                disabledIndeterminateBoxColor = Color.Transparent,
                disabledUncheckedBorderColor = Gray400,
                disabledUncheckedBoxColor = Gray300,
                uncheckedBoxColor = Warning25,
                disabledIndeterminateBorderColor = Warning25,
                uncheckedBorderColor = Warning300,
                uncheckedCheckmarkColor = Color.Transparent
            ),
            enabled = enabled
        )
        ClickableText(
            text = AnnotatedString(text), onClick = onClick, style = Typography.bodyMedium
        )
    }
}