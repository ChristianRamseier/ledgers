package org.ledgers.domain.architecture

import kotlin.js.JsExport

@JsExport
data class LedgerCapability(
    val assetId: AssetId,
    val sourceLedgerId: LedgerId?,
    val accounting: Accounting,
    val sourceLabel: SourceLabel
) {


    init {
        if (sourceLedgerId != null && accounting == Accounting.Native) {
            throw RuntimeException("Native assets cannot have another source ledger.")
        }
        if(sourceLedgerId == null && accounting != Accounting.Native) {
            throw RuntimeException("Non-native assets must have a source ledger.")
        }
    }


}
