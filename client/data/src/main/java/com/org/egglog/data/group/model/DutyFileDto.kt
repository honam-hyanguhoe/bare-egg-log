package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Admin
import com.org.egglog.domain.group.model.FormattedFile
import com.org.egglog.domain.group.model.Group
import kotlinx.serialization.Serializable

@Serializable
data class DutyFileDto(
    val employeeId: String,
    val name: String,
    val work: Map<String, String>
)

fun DutyFileDto.toDomainModel(): FormattedFile {
    return FormattedFile(
        employeeId, name, work
    )
}
