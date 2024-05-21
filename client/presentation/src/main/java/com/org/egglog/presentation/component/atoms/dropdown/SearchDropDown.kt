package com.org.egglog.presentation.component.atoms.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.*

@Composable
fun SearchDropDownHospital(
    list: LazyPagingItems<UserHospital>,
    placeholder: String,
    onSearchChange: (String) -> Unit,
    onSelected: (UserHospital) -> Unit,
    onClickDone: () -> Unit,
    search: String,
    defaultText: String = ""
) {
    val context = LocalContext.current
    SearchableExpandedDropDownMenu(
        lazyPagingItems = list,
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
        onSearchTextFieldClicked = { },
        dropdownItem = { item ->
            Column {
                Text(text = item.hospitalName, style = Typography.bodyMedium)
                Text(text = item.address, style = Typography.displayMedium, color = Gray300)
            }
        },
        onSearchChange = onSearchChange,
        onClickDone = onClickDone,
        search = search,
        defaultText = defaultText
    )
}