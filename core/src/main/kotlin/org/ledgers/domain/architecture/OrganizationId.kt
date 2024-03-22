package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId

data class OrganizationId(override val id: Long): ComponentId {

    companion object {
        var nextId = 0L
        fun nextId(): OrganizationId {
            synchronized(LedgerId.javaClass) {
                nextId++
                return OrganizationId(nextId)
            }
        }
    }
}
