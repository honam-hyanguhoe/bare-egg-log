package com.org.egglog.presentation.domain.setting.navigation

sealed class SettingRoute(val name: String) {
    data object AgreeDetailScreen: SettingRoute("AgreeDetailScreen")
    data object PrivacyDetailScreen: SettingRoute("PrivacyDetailScreen")
    data object SettingScreen: SettingRoute("SettingScreen")
    data object MySettingScreen: SettingRoute("MySettingScreen")
    data object CalendarSettingScreen: SettingRoute("CalendarSettingScreen")
    data object NotificationSettingScreen: SettingRoute("NotificationSettingScreen")
    data object CalendarAddScreen: SettingRoute("CalendarAddScreen")
    data object WorkSettingScreen: SettingRoute("WorkSettingScreen")
    data object AskSettingScreen: SettingRoute("AskSettingScreen")
    data object CertificateBadgeScreen: SettingRoute("CertificateBadgeScreen")

}