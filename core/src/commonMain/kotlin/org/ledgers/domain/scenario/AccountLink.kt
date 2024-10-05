package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.LedgerId

data class AccountLink(
    val sourceLedger: LedgerId,
    val sourceAccounts: List<AccountReference>,
    val targetLedger: LedgerId,
    val targetAccounts: List<AccountReference>
) {

    val reflection: Reflection
        get() = if (sourceAccounts.size == 1 && targetAccounts.size == 1) {
            Reflection.Reconciliation
        } else {
            Reflection.Realignment
        }

}
