package org.ledgers.domain.architecture

import kotlin.js.JsExport

// TODO Get rid of this concept in favor of account links?
@JsExport
data class LedgerCapability(
    // TODO add: val assetType: AssetType? and make assetId optional
    val assetId: AssetId,
    val sourceLedgerId: LedgerId?,
    val accounting: Accounting,
    val sourceLabel: SourceLabel,
) {


    init {
        if (sourceLedgerId != null && accounting == Accounting.Native) {
            throw RuntimeException("Native assets cannot have a source ledger.")
        }
        if (sourceLedgerId == null && accounting != Accounting.Native) {
            throw RuntimeException("Non-native assets must have a source ledger.")
        }
    }

}
