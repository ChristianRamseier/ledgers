package org.ledgers.domain.scenario

import kotlin.js.JsExport

@JsExport
enum class Reflection {
    Reconciliation, // Is performed automatically
    Realignment // Is performed manually
}
