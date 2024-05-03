package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.AssetId
import org.ledgers.dto.serializers.AssetIdSerializer
import org.ledgers.dto.serializers.VersionSerializer


@Serializable
data class AssetDto(
    @Serializable(with = AssetIdSerializer::class)
    val id: AssetId,
    @Serializable(with = VersionSerializer::class)
    val version: Version,
    val name: String,
    val assetType: AssetType
) {

}
