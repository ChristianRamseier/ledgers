package org.ledgers.domain

import java.util.*

data class StoryId(val id: String) {

    companion object {
        fun random(): StoryId {
            return StoryId(UUID.randomUUID().toString())
        }
    }

    override fun toString(): String {
        return id
    }
}
