package com.org.egglog.presentation.component.atoms.profileItem

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.presentation.R
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.*


@Composable
fun ProfileItem(profile: Profile, type: String, createdAt: String ?= null)  {
    val itemType = when(type) {
        "post" -> "post"
        else -> "basic"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(40.widthPercent(LocalContext.current).dp)
                .background(
                    if (itemType.equals("post")) Gray200 else Color.Transparent,
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center) {
            LocalImageLoader(imageUrl = R.drawable.dark, Modifier.size(20.widthPercent(LocalContext.current).dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            // 이름, 인증뱃지
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = profile.name,
                        color = NaturalBlack,
                        style = if (itemType == "post") Typography.displayLarge else Typography.bodyLarge
                )
                if(profile.userId == profile.commentUserId) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "(나)", color = BlueGray400, style = Typography.displayLarge.copy(fontWeight = FontWeight.SemiBold))
                }
                if(itemType.equals("post")) {
                    if (profile.isAuth != null && profile.isAuth) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            Modifier
                                .background(
                                    Warning300,
                                    RoundedCornerShape(20.widthPercent(LocalContext.current).dp)
                                )
                                .padding(horizontal = 8.widthPercent(LocalContext.current).dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LocalImageLoader(imageUrl = R.drawable.small_fry, Modifier.size(12.widthPercent(LocalContext.current).dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                        text = "인증",
                                        color = NaturalWhite,
                                        style = Typography.displayLarge
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 소속, 시간
            Text(
                    text = if (itemType == "post" && createdAt != null) "${profile.hospital} · ${createdAt}" else "${profile.hospital}",
                    color = Gray500,
                    style = Typography.labelMedium.copy(fontSize = 10.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ClientTheme {
        Column {
            val profile = Profile(1,"익명의 구운란1", "전남대병원", true, 1)
            ProfileItem(profile = profile, type = "post")

            val profile2 = Profile(1,"익명의 구운란1", "전남대병원", false)
            ProfileItem(profile = profile2, type = "post")

            val profile3 = Profile(1,"익명의 구운란1", "전남대병원 응급의학과")
            ProfileItem(profile = profile3, type="basic")
        }
    }
}

