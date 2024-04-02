package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class ComponentOnStage(
    val location: Location,
    val reference: ComponentReference
) {
}
