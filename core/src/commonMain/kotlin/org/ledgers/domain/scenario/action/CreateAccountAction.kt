package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.domain.scenario.LedgerAndAccountReference
import kotlin.js.JsExport

@JsExport
class CreateAccountAction(
    override val id: ActionId,
    val ledgerId: LedgerId,
    val accountReference: AccountReference,
    val owner: OrganizationId
) : Action {

    override val type: ActionType get() = ActionType.CreateAccount

    val ledgerAndAccountReference: LedgerAndAccountReference get() = LedgerAndAccountReference(ledgerId, accountReference)

}
