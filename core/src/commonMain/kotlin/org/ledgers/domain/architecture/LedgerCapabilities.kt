package org.ledgers.domain.architecture

import kotlin.js.JsExport

@JsExport
data class LedgerCapabilities(
    val capabilities: List<LedgerCapability>
) {
    companion object {
        val None: LedgerCapabilities = LedgerCapabilities(emptyList())
    }

}
