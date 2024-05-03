package org.ledgers.dto

import kotlinx.serialization.Serializable



@Serializable
data class StorylineDto(
    val chapters: List<ChapterDto>
) {

}
