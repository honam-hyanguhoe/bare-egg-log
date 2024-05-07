package com.org.egglog.client.data

data class PostInfo(
    val postId: Long,
    val title: String,
    val content: String,
    val image1: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    val image4: String?  = null
)