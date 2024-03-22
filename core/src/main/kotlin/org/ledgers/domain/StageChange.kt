package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference

sealed interface StageChange {

    val componentReference: ComponentReference

    data class Add(
        val component: ComponentOnStage
    ) : StageChange {
        override val componentReference: ComponentReference get() = component.reference
    }

    data class Change(
        val component: ComponentOnStage
    ) : StageChange {
        override val componentReference: ComponentReference get() = component.reference
    }

    data class Remove(
        override val componentReference: ComponentReference
    ) : StageChange
}
