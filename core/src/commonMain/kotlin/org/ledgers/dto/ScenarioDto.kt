package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioDto(
    val name: String,
    val steps: List<StepDto>
) {

}
