package com.org.egglog.presentation.component.molecules.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.utils.ArrowLeft
import com.org.egglog.presentation.utils.Close
import com.org.egglog.presentation.utils.Link
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.IconTextButton
import com.org.egglog.presentation.component.atoms.linearIndicator.LinearIndicator
import com.org.egglog.presentation.component.atoms.menus.ScrollableMenus
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun BasicHeader(
    title: String = "무튼 제목임",
    hasTitle: Boolean = false,
    hasArrow: Boolean = false,
    hasLeftClose: Boolean = false,
    hasClose: Boolean = false,
    hasInvitationButton: Boolean = false,
    hasProgressBar: Boolean = false,
    hasMore: Boolean = false,
    onClickBack: () -> Unit = {},
    onClickLink: () -> Unit = {},
    onClickClose: () -> Unit = {},
    onClickMenus: () -> Unit = {},
    options: List<String> = listOf(""),
    selectedOption: String? = null,
    onSelect: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
            .padding(top = 16.heightPercent(LocalContext.current).dp)
    ) {
        Column {
            BasicHeaderContents(
                title = title,
                hasTitle = hasTitle,
                hasArrow = hasArrow,
                hasLeftClose = hasLeftClose,
                hasClose = hasClose,
                hasInvitationButton = hasInvitationButton,
                hasMore = hasMore,
                onClickBack = onClickBack,
                onClickLink = onClickLink,
                onClickClose = onClickClose,
                onClickMenus = onClickMenus,
                options = options,
                selectedOption = selectedOption,
                onSelect = onSelect
            )
            if (hasProgressBar) {
                Spacer(modifier = Modifier.height(10.dp))
                LinearIndicator(0f, true)
            }
        }
    }
}

@Composable
fun BasicHeaderContents(
    title: String,
    hasTitle: Boolean = false,
    hasArrow: Boolean = false,
    hasLeftClose: Boolean = false,
    hasClose: Boolean = false,
    hasInvitationButton: Boolean = false,
    hasMore: Boolean = false,
    onClickBack: () -> Unit = {},
    onClickLink: () -> Unit = {},
    onClickClose: () -> Unit = {},
    onClickMenus: () -> Unit = {},
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasArrow) {
            CustomIconButton(
                size = 25.dp,
                imageVector = ArrowLeft,
                color = NaturalBlack,
                onClick = onClickBack )
        } else if (hasLeftClose) {
            CustomIconButton(
                size = 25.dp,
                imageVector = Close,
                color = NaturalBlack,
                onClick = onClickClose)
        } else {
            Box(modifier = Modifier.size(30.widthPercent(context).dp))
        }

        if (hasTitle) {
            Text(
                text = title,
                style = Typography.bodyLarge,
                color = NaturalBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasInvitationButton) {
                IconTextButton(
                    onClick = onClickLink,
                    width = 80,
                    height = 30,
                    icon = Link,
                    text = "초대링크",
                    textStyle = Typography.labelSmall
                )
            }

            if (hasClose) {
                CustomIconButton(
                    size = 25.dp,
                    imageVector = Close,
                    color = NaturalBlack,
                    onClick = onClickClose)
            } else if (hasMore) {
                ScrollableMenus(
                    options = options,
                    selectedOption = selectedOption,
                    onSelect = onSelect
                )
            } else {
                Box(modifier = Modifier.size(30.widthPercent(context).dp))
            }
        }
    }
}
