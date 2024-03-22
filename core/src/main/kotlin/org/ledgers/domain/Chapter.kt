package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Scenario
import org.ledgers.replaceFirstOrAdd


data class Chapter(
    val changes: List<StageChange>,
    val name: String = "",
    val scenario: Scenario? = null
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

    companion object {
        val Empty = Chapter(emptyList(), "", null)
    }

}
