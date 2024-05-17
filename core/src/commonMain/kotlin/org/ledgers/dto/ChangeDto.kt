package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
@SerialName("Change")
data class ChangeDto(
    val component: ComponentOnStageDto
) : StageChangeDto {
    override val componentReference: ComponentReferenceDto get() = component.reference
}
