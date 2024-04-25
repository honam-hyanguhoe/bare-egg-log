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
import androidx.compose.material3.IconButton
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
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

@Composable
fun BasicHeader(
    title: String,
    hasArrow: Boolean = false,
    hasLeftClose: Boolean = false,
    hasClose: Boolean = false,
    hasInvitationButton: Boolean = false,
    hasProgressBar: Boolean = false,
    hasMore: Boolean = false,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
    ) {
        Column {
            BasicHeaderContents(
                title = title,
                hasArrow = hasArrow,
                hasLeftClose = hasLeftClose,
                hasClose = hasClose,
                hasInvitationButton = hasInvitationButton,
                hasMore = hasMore
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
    hasArrow: Boolean = false,
    hasLeftClose: Boolean = false,
    hasClose: Boolean = false,
    hasInvitationButton: Boolean = false,
    hasMore: Boolean = false,
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
                onClick = { }, modifier = Modifier
                    .size(30.widthPercent(context).dp)
                    .border(1.dp, Warning400)
            ) {
                Icon(
                    ArrowLeft,
                    modifier = Modifier.size(30.widthPercent(context).dp),
                    color = NaturalBlack
                )
            }
        } else if (hasLeftClose) {
            IconButton(
                onClick = { }, modifier = Modifier
                    .size(30.widthPercent(context).dp)
                    .border(1.dp, Warning400)
            ) {
                Icon(
                    Close,
                    modifier = Modifier.size(30.widthPercent(context).dp),
                    color = NaturalBlack
                )
            }
        } else {
            Box(modifier = Modifier.size(30.widthPercent(context).dp))
        }


        Text(
            text = title,
            style = Typography.bodyLarge,
            color = NaturalBlack,
            textAlign = TextAlign.Center,

            modifier =
                if(hasInvitationButton){
                    Modifier.padding(start = 70.widthPercent(context).dp)
                } else {
                    Modifier.padding(start = 0.widthPercent(context).dp)
                }

        )


        Row (
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (hasInvitationButton) {
                Box(modifier = Modifier
                    .height(40.heightPercent(context).dp)
                    .width(70.widthPercent(context).dp)
                    .border(1.dp, Warning400))
            }


            if (hasClose) {
                IconButton(onClick = { }, modifier = Modifier.size(30.widthPercent(context).dp)) {
                    Icon(
                        Close,
                        modifier = Modifier.size(30.widthPercent(context).dp),
                        color = NaturalBlack
                    )
                }
            } else if (hasMore) {
                val groupOptions = listOf("그룹 설정", "그룹원 설정", "그룹 나가기")
                var selectedMenuItem by remember { mutableStateOf<String?>(null) }
                ScrollableMenus(
                    options = groupOptions,
                    selectedOption = selectedMenuItem,
                    onSelect = { selectedMenuItem = it })
            } else {
                Box(modifier = Modifier.size(30.widthPercent(context).dp))
            }

        }


    }
}


@Composable
fun InvitationButton(
    context: Context
) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .height(30.dp),
        border = BorderStroke(1.dp, NaturalBlack),
        shape = ButtonDefaults.outlinedShape,
        colors = ButtonDefaults.buttonColors(NaturalWhite)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Link,
                modifier = Modifier.size(15.widthPercent(context).dp),
                color = NaturalBlack
            )
            Text(text = "초대링크", style = Typography.labelSmall, color = NaturalBlack)
        }
    }
}