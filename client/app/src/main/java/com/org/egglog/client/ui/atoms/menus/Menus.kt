package com.org.egglog.client.ui.atoms.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.MoreVert


@Composable
fun ScrollableMenus( options : List<String>, selectedOption : String?, onSelect : (String) -> Unit ) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedMenuItem by remember { mutableStateOf<String>(options.get(0)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(0.dp)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(MoreVert, modifier = Modifier.size(25.dp))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            offset = DpOffset(-15.dp, -1.dp),
            modifier = Modifier
                .background(Gray100)
                .heightIn(max = 200.dp),
            scrollState = scrollState,
        ) {
            options.forEach { selectedOption ->
                DropdownMenuItem(
                    text = { Text(selectedOption, style = Typography.bodySmall) },
                    onClick = {
                        println("$selectedOption click")
                        selectedMenuItem = selectedOption
                        onSelect(selectedOption)
                        expanded = false
                    },
                )
            }
        }
        LaunchedEffect(expanded) {
            if (expanded) {
                // Scroll to show the bottom menu items.
                scrollState.scrollTo(0)
            }
        }
    }
}


