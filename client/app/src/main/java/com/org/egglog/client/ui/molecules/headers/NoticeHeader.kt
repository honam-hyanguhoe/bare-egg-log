package com.org.egglog.client.ui.molecules.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.buttons.IconButton
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.atoms.menus.ScrollableMenus
import com.org.egglog.client.ui.theme.Gray500
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Success500
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Warning400
import com.org.egglog.client.utils.ArrowDown
import com.org.egglog.client.utils.Notification
import com.org.egglog.client.utils.Search

@Composable
fun NoticeHeader(
    title: String = "",
    hasSearch: Boolean = false,
    hasLogo: Boolean = false,
    hasMenu: Boolean = false,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite),
//            .border(1.dp, Success500)
    ) {
        Column {
            NoticeHeaderContents(
                title = title,
                hasSearch = hasSearch,
                hasLogo = hasLogo,
                hasMenu = hasMenu
            )

        }
    }
}

@Composable
fun NoticeHeaderContents(
    title: String,
    hasSearch: Boolean = false,
    hasLogo: Boolean = false,
    hasMenu: Boolean = false,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
            .padding(horizontal = 10.dp),
//            .border(3.dp, Gray500),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // left
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (hasLogo) {
                // 로고 칸
                LocalImageLoader(imageUrl = R.drawable.simple_logo)
            } else {
                Text(
                    text = title,
                    style = Typography.bodyLarge,
                    color = NaturalBlack,
                    textAlign = TextAlign.Center,
                )

                if (hasMenu) {
                    val communityOptions = listOf("통합", "엑록병원", "호남향우회")
                    var selectedMenuItem by remember { mutableStateOf<String?>(null) }

                    ScrollableMenus(
                        iconShape = ArrowDown,
                        horizontalOffset = -80.dp,
                        options = communityOptions,
                        selectedOption = selectedMenuItem,
                        onSelect = { selectedMenuItem = it })
                }
            }
        }
        // right
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasSearch) {
                IconButton(
                    size = 25.dp,
                    imageVector = Search,
                    color = NaturalBlack,
                    onClick = { /*TODO*/ })
            }
            IconButton(
                size = 25.dp,
                imageVector = Notification,
                color = NaturalBlack,
                onClick = { /*TODO*/ })
        }
    }
}