package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.util.UUID
import kotlin.js.JsExport

@JsExport
data class LedgerId(override val id: String) : ComponentId {


    override fun toString(): String {
        return id
    }

    companion object {
        fun random(): LedgerId {
            return LedgerId(UUID.randomAsString())
        }
    }

}
