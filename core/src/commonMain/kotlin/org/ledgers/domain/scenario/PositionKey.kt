package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.AssetId
import kotlin.js.JsExport

@JsExport
data class PositionKey(val account: LedgerAndAccountReference, val asset: AssetId) {

    override fun toString(): String {
        return "$asset on $account"
    }
}
