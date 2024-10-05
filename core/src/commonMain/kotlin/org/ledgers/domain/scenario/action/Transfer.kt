package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.scenario.*

interface Transfer {
    val asset: AssetId
    val quantity: Quantity
    val fromAccount: LedgerAndAccountReference
    val toAccount: LedgerAndAccountReference
    val fromBooking: Booking get() = Booking(fromAccount, quantity, asset, BookingType.Debit)
    val toBooking: Booking get() = Booking(toAccount, quantity, asset, BookingType.Credit)
    val bookings: List<Booking> get() = listOf(fromBooking, toBooking)
}

