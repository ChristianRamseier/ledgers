package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.Asset

data class Position(val account: Account, val quantity: Number, val asset: Asset, val label: PositionLabel = PositionLabel.NONE) {
}
