package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.AssetId
import kotlin.js.JsExport

@JsExport
data class Position(
    val account: LedgerAndAccountReference,
    val asset: AssetId,
    val quantity: Quantity
) {

    val key: PositionKey get() = PositionKey(account, asset)

    fun withBooking(booking: Booking): Position {
        if (booking.positionKey != key) {
            throw RuntimeException("Cannot apply booking with key ${booking.positionKey} to position with key ${key}")
        }
        return when (booking.type) {
            BookingType.Credit -> withCredit(booking.quantity)
            BookingType.Debit -> withDebit(booking.quantity)
        }
    }

    fun withDebit(debit: Quantity): Position {
        return copy(quantity = quantity - debit)
    }

    fun withCredit(credit: Quantity): Position {
        return copy(quantity = quantity + credit)
    }
}
