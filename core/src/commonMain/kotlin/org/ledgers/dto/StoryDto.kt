package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.StoryId
import org.ledgers.domain.Version
import org.ledgers.dto.serializers.StoryIdSerializer

@Serializable
data class StoryDto(
    @Serializable(with = StoryIdSerializer::class)
    val id: StoryId,
    val name: String,
    val architecture: ArchitectureDto,
    val storyline: StorylineDto
) {

}
