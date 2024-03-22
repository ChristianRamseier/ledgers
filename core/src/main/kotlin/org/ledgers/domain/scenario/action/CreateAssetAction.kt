package org.ledgers.domain.scenario.action

import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.Asset

class CreateAssetAction(
    override val id: ActionId,
    val asset: Asset,
    val ledgerId: LedgerId
) : Action {

}
