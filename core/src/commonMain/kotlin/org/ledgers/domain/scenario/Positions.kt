package org.ledgers.domain.scenario

import org.ledgers.domain.scenario.action.*
import org.ledgers.replaceFirstOrAdd

data class Positions(
    val accounts: List<Account>,
    val positions: List<Position>
) {

    fun withActionsApplied(actions: List<Action>): Positions {
        var updated = this
        actions.forEach { action ->
            updated = updated.withActionApplied(action)
        }
        return updated
    }

    fun withActionApplied(action: Action): Positions {
        return when (action) {

            is CreateAccountAction -> {
                if (hasAccount(action.ledgerAndAccountReference)) {
                    throw RuntimeException("Ledger ${action.ledgerId} already contains an account with reference ${action.accountReference}")
                }
                val account = Account(
                    accountReference = action.accountReference,
                    ledger = action.ledgerId,
                    owner = action.owner
                )
                Positions(
                    accounts = accounts + account,
                    positions = positions
                )
            }

            is IssueAction -> {
                Positions(
                    accounts = accounts,
                    positions = withTransferInPositions(action)
                )
            }

            is ReduceAction -> {
                Positions(
                    accounts = accounts,
                    positions = withTransferInPositions(action)
                )
            }

            is TransferAction -> {
                Positions(
                    accounts = accounts,
                    positions = withTransferInPositions(action)
                )
            }

        }
    }

    private fun withTransferInPositions(transfer: Transfer): List<Position> {

        if(!hasAccount(transfer.fromAccount)) { throw RuntimeException("From account does not exist: ${transfer.fromAccount}") }
        if(!hasAccount(transfer.toAccount)) { throw RuntimeException("To account does not exist: ${transfer.toAccount}") }
        val fromPosition = positions.find { it.account == transfer.fromAccount && it.asset == transfer.asset }?.withDebit( transfer.quantity) ?: Position(transfer.fromAccount, transfer.asset, transfer.quantity, transfer.label)
        val toPosition = positions.find { it.account == transfer.toAccount && it.asset == transfer.asset}?.withCredit( transfer.quantity) ?: Position(transfer.toAccount, transfer.asset, transfer.quantity, transfer.label)

        return positions
            .replaceFirstOrAdd(fromPosition){ it.key == fromPosition.key }
            .replaceFirstOrAdd(toPosition) { it.key == toPosition.key }
    }

    private fun hasAccount(ledgerAndAccountReference: LedgerAndAccountReference): Boolean {
        return accounts.any { it.ledgerAndAccountReference == ledgerAndAccountReference }
    }


    companion object {
        val Empty = Positions(emptyList(), emptyList())
    }

}
