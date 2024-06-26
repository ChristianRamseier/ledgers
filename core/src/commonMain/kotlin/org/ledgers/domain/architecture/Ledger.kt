package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Ledger(
    override val id: LedgerId,
    override val version: Version,
    val name: String,
    val ownerId: OrganizationId,
    val capabilities: LedgerCapabilities
) : Component {

    override val type: ComponentType
        get() = ComponentType.Ledger
}
