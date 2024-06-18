package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.AssetId

data class Booking(
    val account: LedgerAndAccountReference,
    val quantity: Quantity,
    val asset: AssetId,
    val label: PositionLabel,
    val type: BookingType
) {
    val positionKey: PositionKey get() = PositionKey(account, asset, label)
}
