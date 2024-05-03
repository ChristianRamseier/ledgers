package org.ledgers.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ledgers.domain.Story
import kotlin.js.JsExport

@JsExport
fun fromJson(string: String): Story {
    val dto: StoryDto = Json.decodeFromString(string)
    return dto.toDomain()
}

@JsExport
fun toJson(story: Story): String {
    return Json.encodeToString(story.toDto())
}
