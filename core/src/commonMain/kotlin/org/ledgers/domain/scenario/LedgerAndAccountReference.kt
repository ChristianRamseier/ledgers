package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.LedgerId
import kotlin.js.JsExport

@JsExport
data class LedgerAndAccountReference(
    val ledgerId: LedgerId,
    val accountReference: AccountReference
) {
    override fun toString(): String {
        return "$accountReference at $ledgerId"
    }
}
