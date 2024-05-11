package com.org.egglog.presentation.component.molecules.bottomNavigator

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.Calendar
import com.org.egglog.presentation.utils.Community
import com.org.egglog.presentation.utils.Home
import com.org.egglog.presentation.utils.Settings
import com.org.egglog.presentation.utils.User
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun BottomNavigator(
        selectedItem: Int, onItemSelected: (Int) -> Unit
){

    val itemList = listOf(
        Pair("일정", Calendar),
        Pair("그룹", User),
        Pair("홈", Home),
        Pair("커뮤니티", Community),
        Pair("설정", Settings)
    )
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
            .clip(
                RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            ),
        containerColor = NaturalWhite,
        contentColor = NaturalBlack,
        tonalElevation = 0.dp,
            windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        Spacer(modifier = Modifier.padding(2.heightPercent(LocalContext.current).dp))

        itemList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                },
                icon = { Icon(imageVector = item.second, contentDescription = item.first) },
                label = { Text(text = item.first )},
                alwaysShowLabel = true,
                colors = NavigationBarItemColors(
                    selectedIconColor = Warning300,
                    selectedTextColor = Warning300,
                    selectedIndicatorColor = Warning200.copy(alpha = 0.3f),
                    unselectedIconColor = Gray500,
                    unselectedTextColor = Gray500,
                    disabledIconColor = Gray300,
                    disabledTextColor = Gray300
                )
            )
        }
    }
}
