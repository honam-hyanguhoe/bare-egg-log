package com.org.egglog.presentation.domain.myCalendar.navigation

sealed class MyCalendarRoute(
    val name: String
) {

    data object MyCalendarScreen: MyCalendarRoute("MyCalendarScreen")

    data object ExcelListScreen: MyCalendarRoute("ExcelListScreen")

}