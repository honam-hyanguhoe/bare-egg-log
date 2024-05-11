package com.org.egglog.presentation.component.molecules.bottomNavigator

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clip(
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
        ,
        containerColor = Gray25,
        contentColor = NaturalBlack,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
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
                    selectedIndicatorColor = Warning300.copy(alpha = 0.3f),
                    unselectedIconColor = NaturalBlack,
                    unselectedTextColor = NaturalBlack,
                    disabledIconColor = NaturalBlack,
                    disabledTextColor = NaturalBlack
                )
            )
        }
    }
}
