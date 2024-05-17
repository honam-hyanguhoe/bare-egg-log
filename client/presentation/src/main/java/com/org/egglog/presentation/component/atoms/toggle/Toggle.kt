package com.org.egglog.presentation.component.atoms.toggle

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import com.org.egglog.presentation.theme.*

@Composable
fun Toggle(
    toggleEnabled: Boolean = true,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        enabled = toggleEnabled,
        colors = SwitchColors(
            checkedBorderColor = Warning400,
            checkedIconColor = White,
            checkedThumbColor = White,
            checkedTrackColor = Warning400,

            uncheckedBorderColor = Warning400,
            uncheckedIconColor = Warning400,
            uncheckedThumbColor = Warning400,
            uncheckedTrackColor = White,

            disabledCheckedBorderColor = Gray300,
            disabledCheckedIconColor = Gray400,
            disabledCheckedThumbColor = Gray400,
            disabledCheckedTrackColor = Gray300,

            disabledUncheckedBorderColor = Gray400,
            disabledUncheckedIconColor = Gray300,
            disabledUncheckedThumbColor = Gray300,
            disabledUncheckedTrackColor = Gray400
        ),
        checked = checked,
        onCheckedChange = onCheckedChange
    )
}