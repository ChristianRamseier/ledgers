package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
sealed interface ComponentOnStage {

    val reference: ComponentReference

    val type: ComponentType get() = reference.type

}
