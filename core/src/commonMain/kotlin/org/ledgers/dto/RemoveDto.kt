package org.ledgers.dto

import kotlinx.serialization.Serializable



@Serializable
data class RemoveDto(
    override val componentReference: ComponentReferenceDto
) : StageChangeDto
