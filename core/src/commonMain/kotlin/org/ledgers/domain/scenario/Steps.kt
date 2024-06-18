package org.ledgers.domain.scenario

import org.ledgers.add
import org.ledgers.domain.scenario.action.Action
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.replaceAtIndex

data class Steps(
    val steps: List<Step>
) {

    fun withNewStep(step: Step, insertAt: Int = -1): Steps {
        return Steps(steps.add(insertAt, step))
    }

    fun withActionAtStep(action: Action, step: Int): Steps {
        val updatedStep = atStep(step).withAction(action)
        return withChangesInStep(step, updatedStep)
    }

    fun withoutActionAtStep(actionId: ActionId, step: Int): Steps {
        val updatedStep = atStep(step).withoutAction(actionId)
        return withChangesInStep(step, updatedStep)
    }

    fun withDescriptionAtStep(description: String, step: Int): Steps {
        val updatedStep = atStep(step).withDescription(description)
        return withChangesInStep(step, updatedStep)
    }

    fun atStep(step: Int): Step {
        if (step >= 0 && step < steps.size) {
            return steps[step]
        }
        return Step.Empty
    }

    private fun withChangesInStep(step: Int, updatedStep: Step): Steps {
        val updatedSteps = steps.replaceAtIndex(step, updatedStep, Step.Empty)
        return Steps(updatedSteps)
    }

    companion object {
        val None = Steps(emptyList())
    }

}
