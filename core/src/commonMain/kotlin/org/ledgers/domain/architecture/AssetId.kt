package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentId
import org.ledgers.domain.component.ComponentType
import org.ledgers.util.UUID
import kotlin.js.JsExport

@JsExport
data class AssetId(override val id: String) : ComponentId {

    companion object {
        fun random(): AssetId {
            return AssetId(UUID.randomAsString())
        }
    }

}
