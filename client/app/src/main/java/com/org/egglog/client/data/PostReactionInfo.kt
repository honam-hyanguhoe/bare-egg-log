package com.org.egglog.client.data

class PostReactionInfo(val postId: Int, val likeCnt: Int, val commentCnt: Int, val hitCnt: Int = -1, val isLiked: Boolean, val isCommented: Boolean, val isHited: Boolean = false)