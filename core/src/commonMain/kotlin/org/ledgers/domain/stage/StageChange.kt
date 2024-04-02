package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
sealed interface StageChange {

    val componentReference: ComponentReference

    val type: StageChangeType

}
