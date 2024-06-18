package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.scenario.Booking
import org.ledgers.domain.scenario.LedgerAndAccountReference
import org.ledgers.domain.scenario.PositionLabel
import org.ledgers.domain.scenario.Quantity

 class TransferAction(
    override val id: ActionId,
    override val asset: AssetId,
    override val quantity: Quantity,
    override val label: PositionLabel,
    override val fromAccount: LedgerAndAccountReference,
    override val toAccount: LedgerAndAccountReference,
    val intermediateBookings: List<Booking>
) : Action, Transfer {

    override val type: ActionType get() = ActionType.Transfer

     override val bookings: List<Booking> get() = listOf(fromBooking) + intermediateBookings + listOf(toBooking)

}
