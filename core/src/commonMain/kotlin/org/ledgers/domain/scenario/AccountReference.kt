package org.ledgers.domain.scenario

import kotlin.js.JsExport

@JsExport
data class AccountReference(val reference: String) {
    override fun toString(): String {
        return reference
    }
}
