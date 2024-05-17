package com.org.egglog.presentation.utils

import com.org.egglog.presentation.R

enum class MessageUtil(val title: String, val description: String, val imageResId: Int) {
    APPROVE("승인 완료!", "그룹 참여 요청이 승인되었습니다.", R.drawable.small_fry),
    APPLY("신청 완료!", "인증배지 신청이 완료되었습니다.", R.drawable.small_fry),
    REGISTER("접수 완료!", "문의에 대한 답변은 메일에서 안내해드리도록 하겠습니다.", R.drawable.small_fry),
    NO_SEARCH_RESULT("검색 결과가 없습니다", "다른 검색어를 입력해주세요", R.drawable.cry),
    NO_LIST_RESULT("동기화 목록이 없습니다", "업로드를 요청해보세요", R.drawable.cry);
    fun printMessage() {
        println("title: $title, description: $description")
    }
}

