package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Remove(
    override val componentReference: ComponentReference
) : StageChange {

    override val type: StageChangeType get() = StageChangeType.Remove

}
