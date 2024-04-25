package com.org.egglog.client.ui.molecules.headers

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.org.egglog.client.ui.atoms.buttons.IconButton
import com.org.egglog.client.ui.atoms.buttons.IconTextButton
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.linearIndicator.LinearIndicator
import com.org.egglog.client.ui.atoms.menus.ScrollableMenus
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Warning400
import com.org.egglog.client.utils.ArrowLeft
import com.org.egglog.client.utils.Close
import com.org.egglog.client.utils.Link
import com.org.egglog.client.utils.Search
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

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
    onClickBack: () -> Unit,
    onClickLink: () -> Unit,
    onClickClose: () -> Unit,
    onClickMenus: () -> Unit,
    options: List<String> = listOf(""),
    selectedOption: String?,
    onSelect: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
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
                Spacer(modifier = Modifier.height(5.dp))
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
            IconButton(
                size = 25.dp,
                imageVector = ArrowLeft,
                color = NaturalBlack,
                onClick = { onClickBack })
        } else if (hasLeftClose) {
            IconButton(
                size = 25.dp,
                imageVector = Close,
                color = NaturalBlack,
                onClick = { onClickClose })
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
                    onClick = { onClickLink },
                    width = 80,
                    height = 30,
                    icon = Link,
                    text = "초대링크",
                    textStyle = Typography.labelSmall
                )
            }

            if (hasClose) {
                IconButton(
                    size = 25.dp,
                    imageVector = Close,
                    color = NaturalBlack,
                    onClick = { onClickClose })
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
