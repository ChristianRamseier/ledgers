package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.domain.scenario.LedgerAndAccountReference
import org.ledgers.domain.scenario.PositionLabel
import org.ledgers.domain.scenario.Quantity
import kotlin.js.JsExport

/**
 * Reduce quantity of an asset by reducing (i.e. destroying) it
 */
@JsExport
class ReduceAction(
    override val id: ActionId,
    override val asset: AssetId,
    val ledgerId: LedgerId,
    override val quantity: Quantity,
    val sourceAccount: AccountReference,
    val issueAccount: AccountReference
) : Action, Transfer {

    override val label: PositionLabel get() = PositionLabel.None
    override val type: ActionType get() = ActionType.Reduce

    override val fromAccount: LedgerAndAccountReference get() = LedgerAndAccountReference(ledgerId, sourceAccount)
    override val toAccount: LedgerAndAccountReference get() = LedgerAndAccountReference(ledgerId, issueAccount)
}
