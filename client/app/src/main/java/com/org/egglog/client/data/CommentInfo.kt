package com.org.egglog.client.data

import java.time.LocalDateTime

class CommentInfo (val userId: Long, val commentId: Long, val commentContent: String, val hospitalName: String, val tempNickname: String, val commentCreateAt: String, val profileImgUrl: String, val isHospitalAuth: Boolean, val recomment: ArrayList<Any> = ArrayList())