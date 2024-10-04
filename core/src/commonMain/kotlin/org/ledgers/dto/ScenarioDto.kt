package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScenarioDto(
    val steps: List<StepDto>
) {

}
