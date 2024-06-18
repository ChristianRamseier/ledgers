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

@Serializable
@SerialName("Issue")
data class IssueActionDto(
    @Serializable(with = ActionIdSerializer::class)
    override val id: ActionId,
    @Serializable(with = LedgerIdSerializer::class)
    val ledgerId: LedgerId,
    @Serializable(with = AssetIdSerializer::class)
    val asset: AssetId,
    @Serializable(with = QuantitySerializer::class)
    val quantity: Quantity,
    @Serializable(with = AccountReferenceSerializer::class)
    val issueAccount: AccountReference,
    @Serializable(with = AccountReferenceSerializer::class)
    val targetAccount: AccountReference
) : ActionDto {

    override val type: ActionType get() = ActionType.Issue

}
