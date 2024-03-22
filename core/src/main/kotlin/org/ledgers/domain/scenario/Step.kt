package org.ledgers.domain.scenario

import org.ledgers.domain.scenario.action.Action

data class Step(
    val description: String,
    val actions: List<Action>
) {

}
