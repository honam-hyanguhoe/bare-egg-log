package com.org.egglog.client.ui.atoms.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.theme.ClientTheme
import com.org.egglog.client.ui.theme.Typography

@Composable
fun BackgroundCard(margin: Dp, padding: Dp, color: Color, borderRadius: Dp, onClickCard: (() -> Unit) ?= null, children: @Composable () -> Unit) {
    Box(Modifier
            .fillMaxWidth()
            .padding(margin)
            .background(color, RoundedCornerShape(borderRadius))
            .clickable { onClickCard ?: {} }
            .padding(padding)) {
        children()
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ClientTheme {
            // 사용 예시
            BackgroundCard(margin=20.dp, padding=20.dp, color=com.org.egglog.client.ui.theme.Warning200, borderRadius=10.dp, onClickCard = null) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column{
                        Text(text = "에그로그에 친구를 초대하고,", style= Typography.displayLarge)
                        Text(text = "친구와 함께 일정을 공유하세요!", style=Typography.displayLarge)
                    }

                    LocalImageLoader(imageUrl = R.drawable.off, Modifier.size(30.dp))
                }
            }
    }
}