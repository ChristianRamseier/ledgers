package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddDto(
    val component: ComponentOnStageDto
) : StageChangeDto {
    override val componentReference: ComponentReferenceDto get() = component.reference
}
