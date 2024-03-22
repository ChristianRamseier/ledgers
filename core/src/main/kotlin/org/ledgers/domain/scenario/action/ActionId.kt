package org.ledgers.domain.scenario.action

import java.util.*

data class ActionId(val id: String) {

    companion object {
        fun random(): ActionId {
            return ActionId(UUID.randomUUID().toString())
        }
    }

}
