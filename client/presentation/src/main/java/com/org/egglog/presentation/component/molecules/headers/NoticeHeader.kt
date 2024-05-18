package com.org.egglog.presentation.component.molecules.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.utils.ArrowDown
import com.org.egglog.presentation.utils.Notification
import com.org.egglog.presentation.utils.Search
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.menus.ScrollableMenus
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun NoticeHeader(
    title: String = "",
    hasSearch: Boolean = false,
    hasLogo: Boolean = false,
    hasMenu: Boolean = false,
    onClickSearch : () -> Unit = {},
    onClickNotification : () -> Unit = {},
    onClickMenus : () -> Unit = {},
    options: List<String> = listOf(""),
    selectedOption: String? = null,
    onSelect: (String) -> Unit = {},
    horizontalPadding : Int = 0,
    verticalPadding : Int = 0,
    hasNotice : Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
            .background(NaturalWhite)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            NoticeHeaderContents(
                hasNotice = hasNotice,
                title = title,
                hasSearch = hasSearch,
                hasLogo = hasLogo,
                hasMenu = hasMenu,
                onClickSearch = onClickSearch,
                onClickNotification = onClickNotification,
                onClickMenus = onClickMenus,
                options = options,
                selectedOption = selectedOption,
                onSelect = onSelect
            )
        }
    }
}

@Composable
fun NoticeHeaderContents(
    hasNotice : Boolean,
    title: String,
    hasSearch: Boolean = false,
    hasLogo: Boolean = false,
    hasMenu: Boolean = false,
    onClickSearch : () -> Unit = {},
    onClickNotification : () -> Unit = {},
    onClickMenus : () -> Unit = {},
    options: List<String> = listOf(""),
    selectedOption: String?,
    onSelect: (String) -> Unit = {}
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
                    style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalBlack,
                    textAlign = TextAlign.Center,
                )

                if (hasMenu) {
                    ScrollableMenus(
                        iconShape = ArrowDown,
                        horizontalOffset = -80.dp,
                        options = options,
                        selectedOption = selectedOption,
                        onSelect = onSelect)
                }
            }
        }
        // right
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasSearch) {
                CustomIconButton(
                    size = 35.dp,
                    imageVector = Search,
                    color = NaturalBlack,
                    onClick = onClickSearch
                )
            }
            if(hasNotice){
                CustomIconButton(
                    size = 35.dp,
                    imageVector = Notification,
                    color = NaturalBlack,
                    onClick = onClickNotification
                )

            }

        }
    }
}