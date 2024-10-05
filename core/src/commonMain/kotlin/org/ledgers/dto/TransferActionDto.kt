package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.scenario.Quantity
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.scenario.action.ActionType
import org.ledgers.dto.serializers.ActionIdSerializer
import org.ledgers.dto.serializers.AssetIdSerializer
import org.ledgers.dto.serializers.QuantitySerializer

@Serializable
@SerialName("Transfer")
class TransferActionDto(
    @Serializable(with = ActionIdSerializer::class)
    override val id: ActionId,
    @Serializable(with = AssetIdSerializer::class)
    val asset: AssetId,
    @Serializable(with = QuantitySerializer::class)
    val quantity: Quantity,
    val fromAccount: LedgerAndAccountReferenceDto,
    val toAccount: LedgerAndAccountReferenceDto,
    val intermediateBookings: List<BookingDto>
) : ActionDto {

    override val type: ActionType get() = ActionType.Transfer

}
