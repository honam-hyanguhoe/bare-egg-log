package com.org.egglog.component.atoms.labels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.utils.heightPercent
import com.org.egglog.utils.widthPercent
import com.org.egglog.theme.*

@Composable
fun Labels(text: String, size: String? = null, onClick: (()->Unit)? = null) {

    var color = when(text) {
        "Day" -> Day
        "Eve" -> Eve
        "Night" -> Night
        "Off" -> Off
        "교육" -> Education
        "보건" -> Health
        "휴가" -> Vacation
        "None" -> None
        else -> None
    }

    var textStyle = when(size) {
        "big" -> Typography.labelLarge
        else -> Typography.labelMedium
    }

    val width = if (size == "big") 70.widthPercent(LocalContext.current).dp else 42.widthPercent(LocalContext.current).dp
    val height = if (size == "big") 30.heightPercent(LocalContext.current).dp else 18.heightPercent(LocalContext.current).dp
    val border = if (size == "big") 50.widthPercent(LocalContext.current).dp else 4.widthPercent(LocalContext.current).dp

    Box(modifier = Modifier
            .background(color, RoundedCornerShape(border))
            .size(width = width, height = height)
            .run {
                if (onClick != null) {
                    clickable(onClick = onClick)
                } else {
                    this // onClick이 null인 경우 clickable을 적용하지 않음
                }
            },
            contentAlignment = Alignment.Center

    ){
        Text(text= "${text}",
                color = Color.White,
                style= textStyle
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    ClientTheme {
        Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Day", size = "big")
                Labels(text = "Eve", size = "big")
                Labels(text = "Night", size = "big")
                Labels(text = "교육", size = "big")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Labels(text = "Eve")
                Labels(text = "Off")
                Labels(text = "Night")
                Labels(text = "Eve")
                Labels(text = "보건")
                Labels(text = "휴가")
                Labels(text = "None")

            }
        }
    }
}
