package com.org.egglog.client.ui.molecules.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.profileItem.Profile
import com.org.egglog.client.ui.atoms.profileItem.ProfileItem
import com.org.egglog.client.ui.theme.ClientTheme
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.Trash
import com.org.egglog.client.utils.widthPercent

@Composable
fun ProfileDetailCard(profile: Profile, type: String, createdAt: String, onDelete: (() -> Unit) ?= null) {
    Row(Modifier.
            fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {

        val created = when(type){
            "comment" -> createdAt
            else -> null
        }

        val myUserId = 1

        ProfileItem(profile = profile, type = "post", createdAt = created)

        // 내 글일 경우 삭제 버튼 나타남
        if(type.equals("comment") && myUserId == profile.userId) {
            Row(Modifier
                    .clickable(onClick = onDelete ?: {}),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = "삭제",
                        color = com.org.egglog.client.ui.theme.Gray500,
                        style = Typography.displayMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
<<<<<<< Updated upstream
                Icon(imageVector = Trash, modifier = Modifier.width(12.widthPercent(LocalContext.current).dp), color = com.org.egglog.client.ui.theme.Gray500)
=======
                Icon(imageVector = Trash, modifier = Modifier.width(12.widthPercent(LocalContext.current).dp), color = NaturalBlack)
>>>>>>> Stashed changes

            }
        } else if (!type.equals("comment")) {
            // 아닐 경우 profileItem에 등록시간x, profileDetailCard에 등록시간o
            Text(
                    text = "${createdAt}",
                    color = com.org.egglog.client.ui.theme.Gray500,
                    style = Typography.displayMedium)

        }

    }

}


@Preview(showBackground = true)
@Composable
fun Preview() {
    ClientTheme {
        Column(Modifier.fillMaxWidth()) {
            val profile1 = Profile(1,"익명의 구운란1", "전남대병원", true)
            ProfileDetailCard(profile = profile1, type = "comment", createdAt = "1시간 전")
            ProfileDetailCard(profile = profile1, type = "post", createdAt = "1시간 전")

            val profile2 = Profile(2,"익명의 반숙란", "전남대병원", false)
            ProfileDetailCard(profile = profile2, type = "comment", createdAt = "1시간 전")
            ProfileDetailCard(profile = profile2, type = "post", createdAt= "2시간 전")

        }
    }
}