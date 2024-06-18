package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Scenario
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

    fun withScenarioDescription(name: String): Chapter {
        return copy(
            scenario = scenario.withName(name)
        )
    }

    companion object {
        val Empty = Chapter(emptyList(), "", Scenario.Empty)
    }

}
