package org.ledgers.dto

import org.ledgers.domain.Version
import org.ledgers.domain.component.ComponentId
import org.ledgers.domain.component.ComponentType

data class ComponentReferenceDto(
    val type: ComponentType,
    val id: ComponentId,
    val version: Version
) {
}
