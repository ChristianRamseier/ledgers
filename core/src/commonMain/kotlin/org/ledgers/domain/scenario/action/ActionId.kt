package org.ledgers.domain.scenario.action

import org.ledgers.util.UUID


data class ActionId(val id: String) {

    companion object {
        fun random(): ActionId {
            return ActionId(UUID.randomAsString())
        }
    }

}
