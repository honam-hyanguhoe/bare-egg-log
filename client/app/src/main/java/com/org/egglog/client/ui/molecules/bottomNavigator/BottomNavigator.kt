package com.org.egglog.client.ui.molecules.bottomNavigator

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.Gray300
import com.org.egglog.client.ui.theme.Gray400
import com.org.egglog.client.ui.theme.Gray500
import com.org.egglog.client.ui.theme.Gray600
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Success100
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Warning200
import com.org.egglog.client.ui.theme.Warning300
import com.org.egglog.client.ui.theme.Warning400
import com.org.egglog.client.ui.theme.Warning500
import com.org.egglog.client.ui.theme.Warning800
import com.org.egglog.client.ui.theme.White
import com.org.egglog.client.utils.Calendar
import com.org.egglog.client.utils.Community
import com.org.egglog.client.utils.Favorite
import com.org.egglog.client.utils.Home
import com.org.egglog.client.utils.Settings
import com.org.egglog.client.utils.User

@Composable
fun BottomNavigator(
        selectedItem: Int, onItemSelected: (Int) -> Unit
){

    val itemList = listOf<Pair<String, ImageVector>>(
        Pair("일정", Calendar),
        Pair("그룹", User),
        Pair("홈", Home),
        Pair("커뮤니티", Community),
        Pair("설정", Settings)
    )
    NavigationBar(
        modifier = Modifier.fillMaxWidth().shadow(4.dp,
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)).clip(
            RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        ),
        containerColor = NaturalWhite,
        contentColor = NaturalBlack,
        tonalElevation = 0.dp,
            windowInsets = NavigationBarDefaults.windowInsets,
    ) {

        itemList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                          println("bottom tab $index")
                },
                icon = { Icon(imageVector = item.second, contentDescription = item.first) },
                label = { Text(text = item.first, )},
                alwaysShowLabel = true,
                colors = NavigationBarItemColors(
                    selectedIconColor = Warning300,
                    selectedTextColor = NaturalWhite,
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
