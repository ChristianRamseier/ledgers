package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType

data class Organization(
    override val id: OrganizationId,
    override val version: Version,
    val name: String
): Component {

    override val type: ComponentType
        get() = ComponentType.Organization

}
