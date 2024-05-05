package com.org.egglog.presentation.component.organisms.agreeList

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.org.egglog.presentation.component.molecules.listItems.AgreeListItem

@Composable
fun AgreeList(ageChecked: Boolean, setAgeClick: (Boolean) -> Unit, agreeChecked: Boolean, setAgreeClick: (Boolean) -> Unit, infoChecked: Boolean, setInfoClick: (Boolean) -> Unit ) {
    Column(modifier = Modifier) {
        AgreeListItem(text = "만 14세 이상입니다. (필수)", onClick = { setAgeClick(!ageChecked) }, value = ageChecked)
        AgreeListItem(text = "에그로그 약관 동의 (필수)", onClick = { setAgreeClick(!agreeChecked) }, value = agreeChecked, hasText = true, onTextClick = { Log.d("전문보기: ", "hi") })
        AgreeListItem(text = "개인정보 수집 및 이용 안내 (필수)", onClick = { setInfoClick(!infoChecked) }, value = infoChecked, hasText = true, onTextClick = { Log.d("전문보기: ", "hi") })
    }
}