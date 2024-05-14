package com.org.egglog.presentation.component.organisms.workSetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.presentation.component.atoms.buttons.IconButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.molecules.cards.SmallScheduleCard
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.AddBox
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalTime

@Composable
fun WorkManager(
    showCreateBottomSheet: Boolean,
    setShowCreateBottomSheet: (Boolean) -> Unit,
    showModifyBottomSheet: Boolean,
    setShowModifyBottomSheet: (Boolean) -> Unit,
    workTypeList: List<WorkType>,
    title: String,
    color: String,
    onTitleChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
    onClickAdd: () -> Unit,
    onClickModify: () -> Unit,
    onSelected: (String, WorkType) -> Unit,
    selectedWorkType: WorkType?,
    addEnabled: Boolean,
    deleteSelectedWorkType: () -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .padding(horizontal = 8.widthPercent(context).dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(6.heightPercent(context).dp))
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
            Text(text = "근무 추가")
            IconButton(
                color = Blue500,
                imageVector = AddBox,
                enabled = true,
                onClick = { setShowCreateBottomSheet(true) },
                size = 24.widthPercent(context).dp,
            )
        }

        Spacer(modifier = Modifier.height(6.heightPercent(context).dp))
        workTypeList.map {
            SmallScheduleCard(workType = it, isMore = true, groupOptions = if(it.workTag == "ETC") listOf("삭제", "수정") else listOf("수정"), onSelected = onSelected)
        }
        Spacer(modifier = Modifier.height(6.heightPercent(context).dp))

        if(showCreateBottomSheet) BottomSheet(height = 620.heightPercent(context).dp, showBottomSheet = true, onDismiss = {
            setShowCreateBottomSheet(false)
            onColorChange("")
            onTitleChange("")
        }) {
            WorkCreateSheet(
                title = title,
                color = color,
                onTitleChange = onTitleChange,
                onColorChange = onColorChange,
                onStartTimeChange = onStartTimeChange,
                onEndTimeChange = onEndTimeChange,
                onClickAdd = onClickAdd,
                addEnabled = addEnabled
            )
        }

        if(showModifyBottomSheet) BottomSheet(height = if(selectedWorkType?.workTag == "ETC") 620.heightPercent(context).dp else 420.heightPercent(context).dp, showBottomSheet = true, onDismiss = {
            setShowModifyBottomSheet(false)
            onColorChange("")
            onTitleChange("")
            deleteSelectedWorkType()
        }) {
            WorkModifySheet(
                title = title,
                color = color,
                onTitleChange = onTitleChange,
                onColorChange = onColorChange,
                onStartTimeChange = onStartTimeChange,
                onEndTimeChange = onEndTimeChange,
                onClickModify = onClickModify,
                selectedWorkType = selectedWorkType,
                onDismiss = {
                    setShowModifyBottomSheet(false)
                    onColorChange("")
                    onTitleChange("")
                    deleteSelectedWorkType()
                }
            )
        }
    }
}