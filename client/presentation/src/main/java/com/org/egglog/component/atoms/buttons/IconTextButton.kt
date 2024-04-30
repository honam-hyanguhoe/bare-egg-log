package com.org.egglog.component.atoms.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.org.egglog.theme.*
import com.org.egglog.utils.heightPercent
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.icons.Icon

@Composable
fun IconTextButton(
    onClick: () -> Unit,
    width: Int,
    height: Int,
    icon: ImageVector,
    iconSize: Int = 24,
    text: String,
    borderColor: Color = NaturalBlack,
    contentColor: Color = Gray600,
    color: Color = NaturalWhite,
    iconColor: Color = NaturalBlack,
    textColor: Color = NaturalBlack,
    textStyle: TextStyle = Typography.bodyMedium,
    borderRadius: Int = 50,
    paddingWidth: Int = 6,
    paddingHeight: Int = 2
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.width(width.widthPercent(context).dp).height(height.heightPercent(context).dp),
        shape = RoundedCornerShape(borderRadius.widthPercent(context).dp),
        border = BorderStroke(color = borderColor, width = 1.widthPercent(context).dp),
        onClick = onClick,
        contentColor = contentColor,
        color = color
    ) {
        Row(Modifier.padding(horizontal = paddingWidth.widthPercent(context).dp, vertical = paddingHeight.heightPercent(context).dp), Arrangement.Center, Alignment.CenterVertically) {
            Icon(imageVector = icon, modifier = Modifier.size(iconSize.widthPercent(context).dp), color = iconColor)
            Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
            Text(text = text, style = textStyle, color = textColor)
        }
    }
}