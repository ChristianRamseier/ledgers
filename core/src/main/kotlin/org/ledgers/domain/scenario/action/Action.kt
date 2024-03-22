package org.ledgers.domain.scenario.action

sealed interface Action {
    val id: ActionId
}
