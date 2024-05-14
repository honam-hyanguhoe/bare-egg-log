package com.org.egglog.presentation.data

data class EditWorkInfo (
    val workId: Long,
    val workDate: String,
    val workTypeId: Int,
    val isDeleted: Boolean,
)