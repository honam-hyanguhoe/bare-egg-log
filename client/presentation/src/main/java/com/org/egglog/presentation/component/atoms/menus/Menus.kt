package com.org.egglog.presentation.component.atoms.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.MoreVert
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.theme.*


@Composable
fun ScrollableMenus(
    iconShape : ImageVector = MoreVert,
    horizontalOffset : Dp = -6.dp,
    verticalOffset : Dp = 0.dp,
    options: List<String>,
    boxSize: Int = 30,
    iconSize: Int = 25,
    selectedOption: String?,
    onSelect: (String) -> Unit ) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedMenuItem by remember { mutableStateOf<String>(options.get(0)) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(boxSize.widthPercent(context).dp),
//            .border(1.dp, Warning400),
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(iconShape, modifier = Modifier.size(iconSize.dp), color = NaturalBlack)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            offset = DpOffset(horizontalOffset, verticalOffset),
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



