package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.util.UUID

data class OrganizationId(override val id: String) : ComponentId {

    companion object {
        fun random(): OrganizationId {
            return OrganizationId(UUID.randomAsString())
        }
    }
}
