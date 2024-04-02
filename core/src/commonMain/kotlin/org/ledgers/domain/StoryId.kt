package org.ledgers.domain

import org.ledgers.util.UUID
import kotlin.js.JsExport

@JsExport
data class StoryId(val id: String) {

    companion object {
        fun random(): StoryId {
            return StoryId(UUID.randomAsString())
        }
    }

    override fun toString(): String {
        return id
    }
}
