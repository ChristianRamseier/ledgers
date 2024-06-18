package org.ledgers.dto

import kotlinx.serialization.Serializable


@Serializable
data class ChapterDto(
    val name: String,
    val changes: List<StageChangeDto>,
    val scenario: ScenarioDto
) {

}
