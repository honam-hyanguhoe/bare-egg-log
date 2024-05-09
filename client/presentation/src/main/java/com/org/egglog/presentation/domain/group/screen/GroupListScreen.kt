package com.org.egglog.presentation.domain.group.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.domain.group.Group
import com.org.egglog.presentation.component.atoms.buttons.GroupButton
import com.org.egglog.presentation.component.atoms.buttons.MiddleButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.theme.Gray25
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning25
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.AddBox
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun GroupListScreen(

) {
    val groupList = listOf(
        Group(id = 1, master = "김다희", name = "호남 향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울 친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네 친구들", memberCount = 3, image = 3)
    )

    GroupListScreen(
        groupList = groupList
    )


}

@Composable
private fun GroupListScreen(
    groupList: List<Group>
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite)
                .padding(bottom = 24.heightPercent(LocalContext.current).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            NoticeHeader(title = "그룹")

            // LazyColumn으로 그룹 리스트 그리기
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(15.dp)
            ) {
                items(groupList) { group ->
                    GroupButton(
                        onClick = { Log.d("test: ", "clicked!!!") },
                        groupMaster = group.master,
                        groupName = group.name,
                        memberCnt = group.memberCount,
                        groupId = group.id,
                        groupImage = group.image
                    )
                }
            }

            MiddleButton(
                onClick = { Log.d("clicked: ", "clicked!!!!") },
                colors = ButtonColors(
                    contentColor = Warning25,
                    containerColor = Warning300,
                    disabledContentColor = Gray25,
                    disabledContainerColor = Gray300
                )
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(
                        style = Typography.displayLarge,
                        text = "그룹을 만들고 동료를 초대해보세요"
                    )
                    Icon(
                        AddBox,
                        Modifier.size(24.widthPercent(LocalContext.current).dp),
                        NaturalWhite
                    )
                }
            }

            // BottomSheet 추가
            BottomSheet(
                height = 300.dp, // BottomSheet의 높이
                showBottomSheet = false, // BottomSheet를 보여줄지 여부
                onDismiss = { /* TODO: BottomSheet가 닫힐 때 수행할 동작 */ }) {
                Text(text = "hi")
            }
        }
    }
}


@Preview
@Composable
fun GroupListPreviewScreen() {
    val groupList = listOf(
        Group(id = 1, master = "김다희", name = "호남향우회", memberCount = 2, image = 1),
        Group(id = 2, master = "박철수", name = "서울친구들", memberCount = 5, image = 2),
        Group(id = 3, master = "이영희", name = "동네친구들", memberCount = 3, image = 3)
    )

    GroupListScreen(
        groupList = groupList
    )
}
