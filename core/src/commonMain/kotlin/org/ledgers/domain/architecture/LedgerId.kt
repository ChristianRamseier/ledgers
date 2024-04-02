package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.domain.component.ComponentType
import org.ledgers.util.UUID
import kotlin.js.JsExport

@JsExport
data class LedgerId(override val id: String) : ComponentId {

    companion object {
        fun random(): LedgerId {
            return LedgerId(UUID.randomAsString())
        }
    }

}
