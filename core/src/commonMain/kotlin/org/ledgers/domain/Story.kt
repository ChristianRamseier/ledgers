package org.ledgers.domain

import org.ledgers.domain.architecture.Architecture
import kotlin.js.JsExport

@JsExport
data class Story(
    val id: StoryId,
    val version: Version,
    val name: String,
    val architecture: Architecture,
    val storyline: Storyline
) {

    fun getDisplayName(): String {
        if(name.isNullOrBlank()) {
            return "Story #${id}"
        }
        return name
    }

    companion object {
        fun new(): Story {
            return Story(
                id = StoryId.random(),
                version = Version.Zero,
                name = "",
                architecture = Architecture(),
                storyline = Storyline()
            )
        }
    }
}
