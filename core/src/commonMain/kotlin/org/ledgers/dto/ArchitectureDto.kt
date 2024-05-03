package org.ledgers.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArchitectureDto(
    val organizations: List<OrganizationDto>,
    val ledgers: List<LedgerDto>,
    val assets: List<AssetDto>
) {

}
