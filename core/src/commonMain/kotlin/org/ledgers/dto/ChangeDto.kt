package org.ledgers.dto

import kotlinx.serialization.Serializable



@Serializable
data class ChangeDto(
    val component: ComponentOnStageDto
) : StageChangeDto {
    override val componentReference: ComponentReferenceDto get() = component.reference
}
