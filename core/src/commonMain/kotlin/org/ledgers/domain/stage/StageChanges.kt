package org.ledgers.domain.stage

import kotlin.js.JsExport

@JsExport
data class StageChanges(
    val changes: Array<StageChange>,
    val newStage: Stage
)
