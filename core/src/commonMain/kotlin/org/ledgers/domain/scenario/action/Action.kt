package org.ledgers.domain.scenario.action

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
sealed interface Action {
    val id: ActionId
    val type: ActionType

    fun isComponentUsed(reference: ComponentReference): Boolean {
        // This implementation ignores the asset version and works only as long as only one version of an asset exists
        return this is Transfer && this.asset == reference.id
    }

}
