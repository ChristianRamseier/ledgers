package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference

data class ComponentOnStage(
    val location: Location,
    val reference: ComponentReference
) {
}
