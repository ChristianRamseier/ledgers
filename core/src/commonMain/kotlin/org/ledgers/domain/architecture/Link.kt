package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType

data class Link(
    override val id: LinkId,
    override val version: Version,
    val from: LedgerId,
    val to: LedgerId
) : Component {

    override val type: ComponentType get() = ComponentType.Link

}
