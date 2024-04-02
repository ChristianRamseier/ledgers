package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Add(
    val component: ComponentOnStage
) : StageChange {

    override val componentReference: ComponentReference get() = component.reference
    override val type: StageChangeType get() = StageChangeType.Add

}

