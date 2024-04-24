package com.org.egglog.client.ui.molecules.tabBar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TabBar(
        firstContent: @Composable () -> Unit,
        secendContent: @Composable () -> Unit
) {

    var pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0F,
            pageCount = { 2 }
    )
    var tabState by remember {
        mutableStateOf(pagerState.currentPage)
    }
    val titles = listOf("근무", "알람")
    Column(
            modifier = Modifier
                    .fillMaxSize()
    ) {
        SecondaryTabRow(
                selectedTabIndex = tabState,
                containerColor = NaturalWhite,
                contentColor = NaturalBlack,
//                indicator =
                divider = {
                    HorizontalDivider(
                            thickness = 1.dp,
                            color = NaturalWhite
                    )
                }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                        selected = tabState == index,
                        onClick = {
                            // Animate to the selected page when the tab is clicked
                            CoroutineScope(Dispatchers.Main).launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title, style = Typography.bodyMedium) }
                )
            }
        }

        HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(5.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) { page ->
            run {
                if (page == 0) {
                    println("page1")
                    firstContent()
                } else {
                    println("page2")
                    secendContent()
                }
            }
        }

    }
}

