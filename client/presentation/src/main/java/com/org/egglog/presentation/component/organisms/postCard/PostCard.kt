package com.org.egglog.presentation.component.organisms.postCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.PostInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.component.atoms.imageLoader.UrlImageLoader
import com.org.egglog.presentation.component.molecules.cards.ProfileDetailCard
import com.org.egglog.presentation.component.molecules.postReaction.PostReaction
import com.org.egglog.presentation.theme.*

@Composable
fun PostCard(profile: Profile, postInfo: PostInfo, postReaction: PostReactionInfo, onClick: ((Int) -> Unit) ?= null) {

    Column(Modifier
            .padding(bottom = 10.dp)
            .run{
                if(onClick != null) {
                    clickable { onClick(postInfo.postId.toInt()) }
                } else {
                    this
                }
            }
            .drawBehind {
                val borderSize = (1).dp.toPx()
                drawLine(
                        color = Gray200,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = borderSize
                )
            }
            .padding(bottom = 14.dp)
    ) {
        ProfileDetailCard(profile = profile, type = "post", createdAt = "11시간 전")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${postInfo.title}", color = NaturalBlack,style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "${postInfo.content}", color = Gray500, style = Typography.bodyMedium)
        if(postInfo.image1 != null) {
            Spacer(modifier = Modifier.height(16.dp))
            UrlImageLoader(imageUrl = postInfo.image1, Modifier.fillMaxWidth())
        }
        if(postInfo.image2 != null) {
            Spacer(modifier = Modifier.height(10.dp))
            UrlImageLoader(imageUrl = postInfo.image2, Modifier.fillMaxWidth())
        }
        if(postInfo.image3 != null) {
            Spacer(modifier = Modifier.height(10.dp))
            UrlImageLoader(imageUrl = postInfo.image3, Modifier.fillMaxWidth())
        }
        if(postInfo.image4 != null) {
            Spacer(modifier = Modifier.height(10.dp))
            UrlImageLoader(imageUrl = postInfo.image4, Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(16.dp))
        PostReaction(postReactionInfo = postReaction)
    }
}