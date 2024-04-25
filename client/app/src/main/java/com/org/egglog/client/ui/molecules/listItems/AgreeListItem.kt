package com.org.egglog.client.ui.molecules.listItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.checkbox.CheckBoxRow
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.widthPercent

@Composable
fun AgreeListItem(text: String = "", value: Boolean, onClick: (Any) -> Unit, hasText: Boolean = false, onTextClick: (Any) -> Unit = {}) {
    val context = LocalContext.current
    Box(modifier = Modifier.width(340.widthPercent(context).dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            CheckBoxRow(value = value, onClick = onClick, text = text)
            if(hasText) {
                ClickableText(
                    text = AnnotatedString("전문보기"),
                    onClick = onTextClick,
                    style = Typography.displayMedium.copy(color = Gray400)
                )
            }
        }
    }
}