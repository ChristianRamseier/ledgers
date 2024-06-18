package org.ledgers.dto

import kotlinx.serialization.Serializable


@Serializable
sealed interface StageChangeDto {
    val componentReference: ComponentReferenceDto
}
