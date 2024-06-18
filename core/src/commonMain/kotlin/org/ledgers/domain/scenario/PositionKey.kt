package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.AssetId

data class PositionKey(val account: LedgerAndAccountReference, val asset: AssetId, val positionLabel: PositionLabel) {

    override fun toString(): String {
        if(positionLabel != PositionLabel.None) {
            return "$asset on $account with label $positionLabel"
        }
        return "$asset on $account"
    }
}
