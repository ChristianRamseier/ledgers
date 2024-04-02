package org.ledgers.dto

import org.ledgers.domain.StoryId
import org.ledgers.domain.Version

data class StoryDto(
    val id: StoryId,
    val name: String,
    val architecture: ArchitectureDto,
    val storyline: StorylineDto
) {

}
