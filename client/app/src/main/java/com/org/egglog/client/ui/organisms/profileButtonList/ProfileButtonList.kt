package com.org.egglog.client.ui.organisms.profileButtonList

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.UserInfo
import com.org.egglog.client.ui.atoms.buttons.ProfileButton
import com.org.egglog.client.utils.widthPercent

@Composable
fun ProfileButtonList(userInfoList: ArrayList<UserInfo>, selectedList: SnapshotStateList<Int>, myUserId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        userInfoList.forEach { userInfo ->
            ProfileButton(
                userInfo = userInfo,
                isMine = (myUserId == userInfo.userId),
                isSelected = selectedList.contains(userInfo.userId),
                onClick = {
                    if(selectedList.contains(userInfo.userId)) {
                        selectedList[selectedList.indexOf(userInfo.userId)] = 0
                    } else if(selectedList.contains(0)) {
                        selectedList[selectedList.indexOf(0)] = userInfo.userId
                    } else {
                        Log.d("full selected check: ", "해제 후 다시 입력해주세요.")
                    }
                    Log.d("selected check: ", "${selectedList.map { it }}")
                }
            )
            Spacer(modifier = Modifier.padding(2.widthPercent(LocalContext.current).dp))
        }
    }
}