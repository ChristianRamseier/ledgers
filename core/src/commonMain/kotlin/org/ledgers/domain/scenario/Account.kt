package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId

data class Account(
    val accountReference: AccountReference,
    val ledger: LedgerId,
    val owner: OrganizationId
) {
    val ledgerAndAccountReference: LedgerAndAccountReference get() = LedgerAndAccountReference(ledger, accountReference)
}
