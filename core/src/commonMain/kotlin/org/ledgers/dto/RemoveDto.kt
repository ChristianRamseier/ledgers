package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
@SerialName("Remove")
data class RemoveDto(
    override val componentReference: ComponentReferenceDto
) : StageChangeDto
