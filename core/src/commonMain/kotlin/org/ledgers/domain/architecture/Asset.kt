package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Asset(
    override val id: AssetId,
    override val version: Version = Version.Zero,
    val name: String,
    val assetType: AssetType
) : Component {

    override val type: ComponentType get() = ComponentType.Asset

    init {
        if(version != Version.Zero) {
            // Adding support for version would require to make the scenarios version-aware too.
            throw RuntimeException("Versioned assets are not supported.")
        }
    }
}
