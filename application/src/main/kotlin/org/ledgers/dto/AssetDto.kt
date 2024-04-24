package org.ledgers.dto

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.AssetId

data class AssetDto(
    val id: AssetId,
    val version: Version,
    val name: String,
    val assetType: AssetType
) {

}
