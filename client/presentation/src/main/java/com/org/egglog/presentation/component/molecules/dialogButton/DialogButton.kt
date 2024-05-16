package com.org.egglog.presentation.component.molecules.dialogButton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.buttons.IconButton
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.utils.Add
import com.org.egglog.presentation.utils.Remove

@Composable
fun DialogButton(onChange: (Int) -> Unit, interval: Int, nextText: String = "") {
    var result by remember { mutableIntStateOf(interval) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            size = 28.widthPercent(context).dp,
            enabled = true,
            imageVector = Remove,
            color = NaturalWhite,
            onClick = {
                if(result - interval >= 0) result -= interval
                onChange(result)
            },
            backgroundColor = Gray300
        )

        Spacer(modifier = Modifier.padding(6.widthPercent(context).dp))
        Text(text = "$result$nextText")
        Spacer(modifier = Modifier.padding(6.widthPercent(context).dp))

        IconButton(
            size = 28.widthPercent(context).dp,
            imageVector = Add,
            color = NaturalWhite,
            enabled = true,
            onClick = {
                result += interval
                onChange(result)
            },
            backgroundColor = Gray300
        )
    }
}
