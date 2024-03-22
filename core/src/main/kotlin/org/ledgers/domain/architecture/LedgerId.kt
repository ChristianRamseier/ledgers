package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId

data class LedgerId(override val id: Long): ComponentId {

    companion object {
        private var nextId = 0L
        fun nextId(): LedgerId {
            synchronized(LedgerId.javaClass) {
                nextId++
                return LedgerId(nextId)
            }
        }
    }
}
