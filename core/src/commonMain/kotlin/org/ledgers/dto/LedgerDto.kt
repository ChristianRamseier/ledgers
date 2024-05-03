package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.dto.serializers.LedgerIdSerializer
import org.ledgers.dto.serializers.OrganizationIdSerializer
import org.ledgers.dto.serializers.VersionSerializer

@Serializable
data class LedgerDto(
    @Serializable(with = LedgerIdSerializer::class)
    val id: LedgerId,
    @Serializable(with = VersionSerializer::class)
    val version: Version,
    val name: String,
    @Serializable(with = OrganizationIdSerializer::class)
    val ownerId: OrganizationId
) {

}
