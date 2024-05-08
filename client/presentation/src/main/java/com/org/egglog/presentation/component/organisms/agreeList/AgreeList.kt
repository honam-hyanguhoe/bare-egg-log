package com.org.egglog.presentation.component.organisms.agreeList

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.molecules.listItems.AgreeListItem
import com.org.egglog.presentation.component.organisms.dialogs.WebViewDialog
import com.org.egglog.presentation.utils.Favorite

@Composable
fun AgreeList(ageChecked: Boolean, setAgeClick: (Boolean) -> Unit, agreeChecked: Boolean, setAgreeClick: (Boolean) -> Unit, infoChecked: Boolean, setInfoClick: (Boolean) -> Unit ) {
    val openDialogAgree = remember { mutableStateOf(false) }
    val openDialogInfo = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(modifier = Modifier) {
        AgreeListItem(text = "만 14세 이상입니다. (필수)", onClick = { setAgeClick(!ageChecked) }, value = ageChecked)
        AgreeListItem(text = "에그로그 약관 동의 (필수)", onClick = { setAgreeClick(!agreeChecked) }, value = agreeChecked, hasText = true, onTextClick = { openDialogAgree.value = true })
        AgreeListItem(text = "개인정보 수집 및 이용 안내 (필수)", onClick = { setInfoClick(!infoChecked) }, value = infoChecked, hasText = true, onTextClick = { openDialogInfo.value = true })
        when {
            openDialogAgree.value -> {
                WebViewDialog(url = "https://www.egg-log.org/agreement", onDismiss = { openDialogAgree.value = false }, context = context)
            }
            openDialogInfo.value -> {
                WebViewDialog(url = "https://www.egg-log.org/privacy", onDismiss = { openDialogInfo.value = false }, context = context)
            }
        }
    }
}