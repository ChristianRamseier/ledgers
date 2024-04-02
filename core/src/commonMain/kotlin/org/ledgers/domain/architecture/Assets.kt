package org.ledgers.domain.architecture

import kotlin.js.JsExport

@JsExport
data class Assets(val assets: List<Asset> = emptyList()) {

    fun add(asset: Asset): Assets {
        if(assets.any { it.name == asset.name }) {
            throw RuntimeException("Asset with name ${asset.name} already exists")
        }
        return Assets(assets.plus(asset))
    }

    fun remove(name: String): Assets {
        if(assets.none { it.name == name }) {
            throw RuntimeException("Asset with name ${name} not found")
        }
        return Assets(assets.filter { it.name == name })
    }

}
