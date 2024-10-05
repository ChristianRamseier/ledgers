package org.ledgers.domain.scenario.action

import kotlin.js.JsExport

@JsExport
enum class ActionType {
    CreateAccount,
    Issue,
    Reduce,
    Transfer
}
