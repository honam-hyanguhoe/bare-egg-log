package com.org.egglog.client.data

class PostReactionInfo(val postId: Long, val likeCnt: Long, val commentCnt: Long, val hitCnt: Long = -1, val isLiked: Boolean, val isCommented: Boolean)