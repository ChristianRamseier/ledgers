package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface ComponentOnStageDto {
    val reference: ComponentReferenceDto
}
