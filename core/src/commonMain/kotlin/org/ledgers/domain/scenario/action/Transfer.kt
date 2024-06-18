package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.scenario.*

interface Transfer {
    val asset: AssetId
    val quantity: Quantity
    val label: PositionLabel
    val fromAccount: LedgerAndAccountReference
    val toAccount: LedgerAndAccountReference
    val fromBooking: Booking get() = Booking(fromAccount, quantity, asset, label, BookingType.Debit)
    val toBooking: Booking get() = Booking(toAccount, quantity, asset, label, BookingType.Credit)
    val bookings: List<Booking> get() = listOf(fromBooking, toBooking)
}

