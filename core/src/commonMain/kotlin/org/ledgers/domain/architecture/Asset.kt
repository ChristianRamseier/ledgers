package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import kotlin.js.JsExport

@JsExport
data class Asset(val name: String, val assetType: AssetType) {
}
