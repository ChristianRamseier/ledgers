package org.ledgers.dto

import kotlinx.serialization.Serializable



@Serializable
data class ComponentOnStageDto(
    val location: BoxDto,
    val reference: ComponentReferenceDto
) {

}
