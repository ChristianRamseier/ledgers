package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
data class StepDto(
    val description: String,
    val actions: List<ActionDto>
) {

}
