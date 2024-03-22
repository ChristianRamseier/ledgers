package org.ledgers.dto

data class ArchitectureDto(
    val organizations: List<OrganizationDto>,
    val ledgers: List<LedgerDto>,
    val assets: List<AssetDto>
) {

}
