package org.ledgers.dto

import org.ledgers.domain.AssetType

data class AssetDto(
    val name: String,
    val assetType: AssetType
) {

}
