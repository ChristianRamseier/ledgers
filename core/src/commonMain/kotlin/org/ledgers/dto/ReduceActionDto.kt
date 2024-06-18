package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.domain.scenario.Quantity
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.scenario.action.ActionType
import org.ledgers.dto.serializers.*

/**
 * Reduce quantity of an asset by reducing (i.e. destroying) it
 */
@Serializable
@SerialName("Reduce")
class ReduceActionDto(
    @Serializable(with = ActionIdSerializer::class)
    override val id: ActionId,
    @Serializable(with = AssetIdSerializer::class)
    val asset: AssetId,
    @Serializable(with = LedgerIdSerializer::class)
    val ledgerId: LedgerId,
    @Serializable(with = QuantitySerializer::class)
    val quantity: Quantity,
    @Serializable(with = AccountReferenceSerializer::class)
    val sourceAccount: AccountReference,
    @Serializable(with = AccountReferenceSerializer::class)
    val issueAccount: AccountReference
) : ActionDto {

    override val type: ActionType get() = ActionType.Reduce

}
