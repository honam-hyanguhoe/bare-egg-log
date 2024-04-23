package com.org.egglog.client.ui.atoms.buttons

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.imageLoader.UrlImageLoader
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun DashedBorder(modifier: Modifier = Modifier, color: Color = NaturalBlack, strokeWidth: Float = 2f) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            drawLine(
                color = color,
                strokeWidth = strokeWidth,
                pathEffect = pathEffect,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2)
            )
        }
    }
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 4.dp,
    gapWidth: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, this)

    val path = Path()
    path.addOutline(outline)

    val stroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
            phase = 0f
        )
    )

    this.drawContent()

    drawPath(
        path = path,
        style = stroke,
        color = color
    )
}



@Composable
fun ProfileButton(onClick: () -> Unit, profileImgUrl: String, userId: Int, userName: String, isSelected: Boolean, isMine: Boolean){
    val context = LocalContext.current
    val stroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
//    val stroke = BorderStroke(width = 1.dp, color = NaturalBlack, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f))
    val dashModifier: Modifier = if(isSelected) Modifier.height(112.heightPercent(context).dp).width(78.widthPercent(context).dp) else Modifier.height(112.heightPercent(context).dp).width(78.widthPercent(context).dp).dashedBorder(
        NaturalBlack, RoundedCornerShape(30.widthPercent(context).dp))
    val name = if (isMine) "$userName(나)" else userName
    Surface(onClick = onClick, modifier = dashModifier, shape = RoundedCornerShape(30.widthPercent(context).dp)) {
        Column(Modifier.fillMaxWidth().padding(6.widthPercent(context).dp), Arrangement.SpaceAround, Alignment.CenterHorizontally) {
            UrlImageLoader(imageUrl = profileImgUrl, modifier = Modifier.size(44.widthPercent(LocalContext.current).dp).clip(CircleShape))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
                if(isMine) Box(modifier = Modifier.size(5.widthPercent(LocalContext.current).dp).border(width = 0.dp, color = Warning300, shape = CircleShape).background(Warning300, CircleShape))
                Text(style = Typography.bodyMedium, text = name)
            }
        }
    }
}