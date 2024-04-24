package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Asset(
    override val id: AssetId,
    override val version: Version,
    val name: String,
    val assetType: AssetType
) : Component {

    override val type: ComponentType get() = ComponentType.Asset
}
