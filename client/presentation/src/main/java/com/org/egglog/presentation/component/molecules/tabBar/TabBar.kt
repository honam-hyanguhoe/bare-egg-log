package com.org.egglog.presentation.component.molecules.tabBar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TabBar(
    titles: List<String>,
    firstContent: @Composable () -> Unit,
    secendContent: @Composable () -> Unit
) {

    var pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0F,
        pageCount = { titles.size }
    )

    var coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = NaturalWhite,
            contentColor = NaturalBlack,
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = NaturalWhite
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        println("page index ${pagerState.currentPage}")
                        // Animate to the selected page when the tab is clicked
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title, style = Typography.bodyMedium) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSize = PageSize.Fill,
            contentPadding = PaddingValues(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = true,
        ) { page ->

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


