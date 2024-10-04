package org.ledgers.domain.scenario.action

import kotlin.js.JsExport

@JsExport
sealed interface Action {
    val id: ActionId
    val type: ActionType
}
