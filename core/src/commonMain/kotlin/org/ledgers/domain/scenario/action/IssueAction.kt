package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.domain.scenario.LedgerAndAccountReference
import org.ledgers.domain.scenario.PositionLabel
import org.ledgers.domain.scenario.Quantity

/**
 * Increase quantity of an asset by issuing (i.e. creating) it
 */
class IssueAction(
    override val id: ActionId,
    val ledgerId: LedgerId,
    override val asset: AssetId,
    override val quantity: Quantity,
    val issueAccount: AccountReference,
    val targetAccount: AccountReference
) : Action, Transfer {

    override val label: PositionLabel get() = PositionLabel.None
    override val type: ActionType get() = ActionType.Issue

    override val fromAccount: LedgerAndAccountReference get() = LedgerAndAccountReference(ledgerId, issueAccount)
    override val toAccount: LedgerAndAccountReference get() = LedgerAndAccountReference(ledgerId, targetAccount)

}
