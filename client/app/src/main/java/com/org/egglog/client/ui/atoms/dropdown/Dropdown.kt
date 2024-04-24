package com.org.egglog.client.ui.atoms.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.Gray200
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.ArrowDown
import com.org.egglog.client.utils.widthPercent


@Composable
fun DropdownList(options: List<String>, selectedOption: String?, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedMenuItem by remember { mutableStateOf<String>(options[0]) }

    Box() {
        Box(Modifier
                .background(Gray100, RoundedCornerShape(10.dp))
                .border(1.dp, Gray200, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .clickable(onClick = {
                    expanded = !expanded
                }))
                 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${selectedMenuItem}", color = NaturalBlack, style = Typography.displayMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = ArrowDown, modifier = Modifier.size(14.widthPercent(LocalContext.current).dp), color = NaturalBlack)
            }
        }
        DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier
                        .background(Gray100),
        ) {
            options.forEach {
                DropdownMenuItem(
                        text = { Text(it, style = Typography.displayMedium) },
                        onClick = {
                            selectedMenuItem = it
                            onSelect(it)
                            expanded = false
                        },
                )
            }
        }
    }
}