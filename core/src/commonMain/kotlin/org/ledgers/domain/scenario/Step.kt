package org.ledgers.domain.scenario

import org.ledgers.domain.scenario.action.Action
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.replaceFirstOrAdd

data class Step(
    val description: String,
    val actions: List<Action>
) {

    fun withAction(action: Action): Step {
        return copy(
            actions = actions.replaceFirstOrAdd(action) { it.id == action.id }
        )
    }

    fun withoutAction(actionId: ActionId): Step {
        return copy(
            actions = actions.filterNot { it.id == actionId }
        )
    }

    fun withDescription(description: String): Step {
        return copy(
            description = description
        )
    }

    companion object {
        val Empty = Step("", emptyList())
    }
}
