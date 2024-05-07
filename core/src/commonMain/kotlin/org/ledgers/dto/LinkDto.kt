package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.LinkId
import org.ledgers.dto.serializers.LedgerIdSerializer
import org.ledgers.dto.serializers.LinkIdSerializer
import org.ledgers.dto.serializers.VersionSerializer


@Serializable
data class LinkDto(
    @Serializable(with = LinkIdSerializer::class)
    val id: LinkId,
    @Serializable(with = VersionSerializer::class)
    val version: Version,
    @Serializable(with = LedgerIdSerializer::class)
    val from: LedgerId,
    @Serializable(with = LedgerIdSerializer::class)
    val to: LedgerId
) {

}
