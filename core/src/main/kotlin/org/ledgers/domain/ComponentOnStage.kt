package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference

data class ComponentOnStage(
    val location: Location,
    val reference: ComponentReference
) {
}
