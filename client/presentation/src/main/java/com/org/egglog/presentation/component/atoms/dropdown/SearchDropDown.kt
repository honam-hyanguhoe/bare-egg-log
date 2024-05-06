package com.org.egglog.presentation.component.atoms.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.*

@Composable
fun SearchDropDownHospital(
    list: MutableList<UserHospital>,
    placeholder: String,
    onSelected: (UserHospital) -> Unit
) {
    val context = LocalContext.current
    SearchableExpandedDropDownMenu(
        listOfItems = list,
        modifier = Modifier.fillMaxWidth(),
        onDropDownItemSelected = { item ->
            onSelected(item)
        },
        enable = true,
        placeholder = { Text(text = placeholder) },
        openedIcon = ArrowDown,
        closedIcon = ArrowUp,
        parentTextFieldCornerRadius = 8.widthPercent(context).dp,
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = Gray300,
            unfocusedContainerColor = White,
            unfocusedTextColor = NaturalBlack,
            unfocusedTrailingIconColor = White,
            focusedPlaceholderColor = Gray300,
            focusedContainerColor = White,
            focusedTextColor = NaturalBlack,
            focusedTrailingIconColor = White,
            unfocusedIndicatorColor = Gray400,
            focusedIndicatorColor = Warning400
        ),
        defaultItem = { },
        onSearchTextFieldClicked = { },
        dropdownItem = { item ->
            Column {
                Text(text = item.hospitalName, style = Typography.bodyMedium)
                Text(text = item.address, style = Typography.displayMedium, color = Gray300)
            }
        }
    )
}