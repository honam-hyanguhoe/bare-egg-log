package com.org.egglog.client.ui.atoms.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.BlueGray900
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.utils.Close
import com.org.egglog.client.utils.Edit

@Composable
fun FloatingButton(
    onClick: () -> Unit,
) {

    val fabInteractionSource = remember {
        MutableInteractionSource()
    }
    val (isVisible, setVisible) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, NaturalBlack),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.padding(5.dp),
            shape = FloatingActionButtonDefaults.largeShape,
            containerColor = BlueGray900,
            contentColor = NaturalWhite,
            elevation = FloatingActionButtonDefaults.elevation(),
//        interactionSource =
        ) {
            Icon(imageVector = if (isVisible) Close else Edit, contentDescription = "fab Icon")
        }
    }
}


//FloatingActionButton(
//onClick = { setVisible(!isVisible) },
//modifier = Modifier
//.height(40.dp)
//.width(90.dp),
//shape = FloatingActionButtonDefaults.extendedFabShape,
//containerColor = BlueGray900,
//contentColor = NaturalWhite,
//elevation = FloatingActionButtonDefaults.elevation(8.dp),
//interactionSource = fabInteractionSource
//) {
//    Text(text = "근무 일정 추가")
//}
//FloatingActionButton(
//onClick = { /*TODO*/ },
//modifier = Modifier
//.height(40.dp)
//.width(90.dp),
//shape = FloatingActionButtonDefaults.extendedFabShape,
//containerColor = BlueGray900,
//contentColor = NaturalWhite,
//elevation = FloatingActionButtonDefaults.elevation(),
//) {
//    Text(text = "개인 일정 추가")
//}
//FloatingActionButton(
//onClick = { /*TODO*/ },
//modifier = Modifier
//.height(40.dp)
//.width(90.dp),
//shape = FloatingActionButtonDefaults.extendedFabShape,
//containerColor = BlueGray900,
//contentColor = NaturalWhite,
//elevation = FloatingActionButtonDefaults.elevation(),
//) {
//    Text(text = "개인 일정 추가")
//}