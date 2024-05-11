package com.org.egglog.presentation.domain.setting.navigation

sealed class SettingRoute(val name: String) {
    data object AgreeDetailScreen: SettingRoute("AgreeDetailScreen")
    data object PrivacyDetailScreen: SettingRoute("PrivacyDetailScreen")
    data object SettingScreen: SettingRoute("SettingScreen")
    data object MySettingScreen: SettingRoute("MySettingScreen")
    data object CalendarSettingScreen: SettingRoute("CalendarSettingScreen")
    data object CalendarAddScreen: SettingRoute("CalendarAddScreen")

}