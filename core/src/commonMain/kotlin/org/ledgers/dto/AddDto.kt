package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Add")
data class AddDto(
    val component: ComponentOnStageDto
) : StageChangeDto {
    override val componentReference: ComponentReferenceDto get() = component.reference
}
