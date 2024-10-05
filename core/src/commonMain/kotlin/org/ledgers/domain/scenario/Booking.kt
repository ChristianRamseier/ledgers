package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.AssetId
import kotlin.js.JsExport

@JsExport
data class Booking(
    val account: LedgerAndAccountReference,
    val quantity: Quantity,
    val asset: AssetId,
    val type: BookingType
) {
    val positionKey: PositionKey get() = PositionKey(account, asset)
}
