package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType

data class Ledger(
    override val id: LedgerId,
    override val version: Version,
    val name: String,
    val ownerId: OrganizationId
) : Component {

    override val type: ComponentType
        get() = ComponentType.Ledger
}
