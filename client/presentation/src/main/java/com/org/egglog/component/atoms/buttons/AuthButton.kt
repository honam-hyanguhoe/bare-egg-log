package com.org.egglog.component.atoms.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.R
import com.org.egglog.utils.widthPercent

@Composable
fun AuthButton(type: String, onClick: () -> Unit) {
    val imageResId = when (type) {
        "kakao" -> R.drawable.kakao
        "naver" -> R.drawable.naver
        "google" -> R.drawable.google
        else -> R.drawable.cry // 기본 이미지 설정
    }

    Surface(
        onClick = onClick,
        modifier = Modifier.size(56.widthPercent(LocalContext.current).dp),
        shape = RoundedCornerShape(10.widthPercent(LocalContext.current).dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}