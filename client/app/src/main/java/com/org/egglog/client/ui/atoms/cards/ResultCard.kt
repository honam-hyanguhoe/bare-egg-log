package com.org.egglog.client.ui.atoms.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.MessageUtil

@Composable
fun ResultCard(message: MessageUtil) {
    val messageTitle = message.title
    val messageDescription = message.description
    val messageImageUrl = message.imageResId
    Card(
        colors = CardColors(
            containerColor = NaturalWhite,
            contentColor = NaturalBlack,
            disabledContainerColor = NaturalWhite,
            disabledContentColor = NaturalBlack
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalImageLoader(imageUrl = messageImageUrl, modifier = Modifier.size(55.dp, 45.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = messageTitle,
                style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier.width(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = messageDescription,
                    style = Typography.displayMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun ResultCardPreview() {
    MaterialTheme {
        ResultCard(message = MessageUtil.APPROVE)
    }
}