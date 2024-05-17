package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentId
import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Stage(
    val components: List<ComponentOnStage> = emptyList()
) {

    fun has(reference: ComponentReference): Boolean {
        return components.any { it.reference == reference }
    }

    fun getById(id: ComponentId): ComponentOnStage {
        return components.find { it.reference.id == id } ?: throw RuntimeException("No component on stage by id $id")
    }

    fun getChangesThatLeadTo(otherStage: Stage): List<StageChange> {
        val oldById = this.components.associateBy { it.reference.id }
        val newById = otherStage.components.associateBy { it.reference.id }
        val toRemove = (oldById.keys - newById.keys).map { Remove(oldById[it]!!.reference) }
        val toAdd = (newById.keys - oldById.keys).map { Add(newById[it]!!) }
        val toChange = (newById.keys.intersect(oldById.keys))
            .filter { newById[it] != oldById[it] }
            .map { Change(newById[it]!!) }
        return toRemove + toAdd + toChange
    }

}
