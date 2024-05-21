package com.org.egglog.presentation.component.molecules.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.R
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.*

@Composable
fun ExcelCard(color: String, date: String, name: String, onClickCard: () -> Unit) {
    val context = LocalContext.current

    val boxColor = when(color) {
        "green" -> Success600
        else -> NaturalWhite
    }

    Box(modifier = Modifier
        .size(140.widthPercent(context).dp)
        .background(boxColor, RoundedCornerShape(10.dp))
        .border(1.dp, Success600, RoundedCornerShape(10.dp))
        .clickable { onClickCard() }
        .padding(10.dp, 16.dp)
            ) {
        Column(Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LocalImageLoader(imageUrl = R.drawable.excel, Modifier.size(36.widthPercent(context).dp))
                Text(date, color = if(color.equals("green")) NaturalWhite else Success600, style = Typography.displayLarge, textAlign = TextAlign.Right)
            }
            Text("[작성자] ${name}", color = if(color.equals("green")) NaturalWhite else Success600, style = Typography.displayLarge)
        }
    }
}

@Preview
@Composable
fun ExcelCardTest() {
    MaterialTheme {
        ExcelCard(color = "green", date = "2024-03-03", name = "김싸피") {
            println("안녕하세요")
        }
    }
}