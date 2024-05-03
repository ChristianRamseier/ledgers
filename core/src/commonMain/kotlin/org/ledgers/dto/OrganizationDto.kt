package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.dto.serializers.OrganizationIdSerializer
import org.ledgers.dto.serializers.VersionSerializer



@Serializable
data class OrganizationDto(
    @Serializable(with = OrganizationIdSerializer::class)
    val id: OrganizationId,
    @Serializable(with = VersionSerializer::class)
    val version: Version,
    val name: String
) {

}
