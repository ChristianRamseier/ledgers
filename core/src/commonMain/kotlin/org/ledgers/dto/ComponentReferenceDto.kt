package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.Version
import org.ledgers.domain.component.ComponentType
import org.ledgers.dto.serializers.VersionSerializer


@Serializable
data class ComponentReferenceDto(
    val type: ComponentType,
    val id: String,
    @Serializable(with = VersionSerializer::class)
    val version: Version
) {
}
