package org.ledgers.domain.component

import org.ledgers.domain.Version

data class ComponentReference(
    val type: ComponentType,
    val id: ComponentId,
    val version: Version
) {
}
