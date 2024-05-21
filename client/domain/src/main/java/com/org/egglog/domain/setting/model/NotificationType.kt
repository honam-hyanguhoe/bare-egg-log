package com.org.egglog.domain.setting.model

enum class NotificationType {
    CALENDAR, GROUP, BOARD, SYSTEM
}

fun NotificationType.toTitle(): String {
    return when (this) {
        NotificationType.CALENDAR -> "캘린더 알림"
        NotificationType.GROUP -> "그룹 알림"
        NotificationType.BOARD -> "게시판 알림"
        NotificationType.SYSTEM -> "기타 알림"
    }
}

fun NotificationType.toBody(): String {
    return when (this) {
        NotificationType.CALENDAR -> "개인 일정, 근무 일정에 대한 알림 수신"
        NotificationType.GROUP -> "댓글, 대댓글에 대한 알림 수신"
        NotificationType.BOARD -> "새 멤버 가입, 새 글, 새 근무 파일에 대한 알림 수신"
        NotificationType.SYSTEM -> "인증 승인, 문의 답변에 대한 알림 수신"
    }
}