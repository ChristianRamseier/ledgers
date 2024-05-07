package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.Accounting
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.SourceLabel
import org.ledgers.dto.serializers.AssetIdSerializer
import org.ledgers.dto.serializers.LedgerIdSerializer
import org.ledgers.dto.serializers.SourceLabelSerializer

@Serializable
data class LedgerCapabilityDto(
    @Serializable(with = AssetIdSerializer::class)
    val assetId: AssetId,
    @Serializable(with = LedgerIdSerializer::class)
    val sourceLedgerId: LedgerId?,
    val accounting: Accounting,
    @Serializable(with = SourceLabelSerializer::class)
    val sourceLabel: SourceLabel

) {

}
