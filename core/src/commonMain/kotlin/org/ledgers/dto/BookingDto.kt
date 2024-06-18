package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.scenario.BookingType
import org.ledgers.domain.scenario.PositionLabel
import org.ledgers.domain.scenario.Quantity
import org.ledgers.dto.serializers.AssetIdSerializer
import org.ledgers.dto.serializers.PositionLabelSerializer
import org.ledgers.dto.serializers.QuantitySerializer

@Serializable
data class BookingDto(
    val account: LedgerAndAccountReferenceDto,
    @Serializable(with = QuantitySerializer::class)
    val quantity: Quantity,
    @Serializable(with = AssetIdSerializer::class)
    val asset: AssetId,
    @Serializable(with = PositionLabelSerializer::class)
    val label: PositionLabel,
    val type: BookingType
)
