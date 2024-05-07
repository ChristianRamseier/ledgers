package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.util.UUID
import kotlin.js.JsExport

@JsExport
data class LinkId(override val id: String) : ComponentId {

    override fun toString(): String {
        return id
    }

    companion object {
        fun random(): LinkId {
            return LinkId(UUID.randomAsString())
        }
    }

}
