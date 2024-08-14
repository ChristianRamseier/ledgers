package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Scenario
import org.ledgers.domain.scenario.Step
import org.ledgers.domain.scenario.action.Action
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.stage.StageChange
import org.ledgers.replaceFirstOrAdd
import kotlin.js.JsExport

@JsExport
data class Chapter(
    val changes: List<StageChange>,
    val name: String = "",
    val scenario: Scenario = Scenario.Empty
) {

    fun withName(name: String): Chapter {
        return copy(name = name)
    }

    fun withChange(change: StageChange): Chapter {
        val updated = changes.replaceFirstOrAdd(change) { it.componentReference == change.componentReference }
        return Chapter(updated, name, scenario)
    }

    fun withoutChangesTo(reference: ComponentReference): Chapter {
        val updated = changes.filter { it.componentReference != reference }
        return Chapter(updated, name, scenario)
    }

    fun findStageChange(reference: ComponentReference): StageChange? {
        return changes.find { it.componentReference == reference }
    }

    fun withNewScenarioStep(step: Step, insertAt: Int = -1): Chapter {
        return copy(scenario = scenario.withNewStep(step, insertAt))
    }

    fun withActionAtScenarioStep(action: Action, step: Int): Chapter {
        return copy(scenario = scenario.withActionAtStep(action, step))
    }

    fun withoutActionAtStep(actionId: ActionId, step: Int): Chapter {
        return copy(scenario = scenario.withoutActionAtStep(actionId, step))
    }

    fun withDescriptionAtStep(description: String, step: Int): Chapter {
        return copy(scenario = scenario.withDescriptionAtStep(description, step))
    }

    fun withScenarioDescription(name: String): Chapter {
        return copy(
            scenario = scenario.withName(name)
        )
    }

    fun withoutScenarioStep(step: Int): Chapter {
        return copy(scenario = scenario.withoutStep(step))
    }

    fun getChangesAsArray(): Array<StageChange> {
        return changes.toTypedArray()
    }

    companion object {
        val Empty = Chapter(emptyList(), "", Scenario.Empty)
    }

}
