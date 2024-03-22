package org.ledgers.dto

data class ChapterDto(
    val name: String,
    val changes: List<StageChangeDto>,
    val scenario: ScenarioDto?
) {

}
