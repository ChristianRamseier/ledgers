package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.util.UUID

data class LedgerId(override val id: String) : ComponentId {

    companion object {
        fun random(): LedgerId {
            return LedgerId(UUID.randomAsString())
        }
    }
}
