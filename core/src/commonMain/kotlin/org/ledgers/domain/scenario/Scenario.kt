package org.ledgers.domain.scenario

import org.ledgers.domain.scenario.action.Action
import org.ledgers.domain.scenario.action.ActionId

data class Scenario(
    val name: String,
    val steps: Steps
) {

    fun getPositionsAtStep(atStep: Int): Positions {
        var positions = Positions.Empty
        0.rangeTo(atStep).forEach { step ->
            positions = positions.withActionsApplied(steps.atStep(step).actions)
        }
        return positions
    }

    fun withName(name: String): Scenario {
        return copy(
            name = name
        )
    }

    fun withNewStep(step: Step, insertAt: Int = -1): Scenario {
        return copy(steps = steps.withNewStep(step, insertAt))
    }

    fun withActionAtStep(action: Action, step: Int): Scenario {
        return copy(steps = steps.withActionAtStep(action, step))
    }

    fun withoutActionAtStep(actionId: ActionId, step: Int): Scenario {
        return copy(steps = steps.withoutActionAtStep(actionId, step))
    }

    fun withDescriptionAtStep(description: String, step: Int): Scenario {
        return copy(steps = steps.withDescriptionAtStep(description, step))
    }

    fun withoutStep(step: Int): Scenario {
        return copy(steps = steps.withoutStep(step))
    }

    companion object {
        val Empty = Scenario("", Steps.None)
    }

}
