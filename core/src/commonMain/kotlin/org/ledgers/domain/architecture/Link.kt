package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Link(
    override val id: LinkId,
    override val version: Version,
    val from: LedgerId,
    val to: LedgerId
) : Component {

    override val type: ComponentType get() = ComponentType.Link

    init {
        if(from == to) {
            throw RuntimeException("Ledgers cannot link themselves.")
        }
    }

}
