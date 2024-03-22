package org.ledgers.domain.architecture

class Assets {

    val assets: List<Asset>

    constructor() {
        assets = emptyList()
    }

    constructor(assets: List<Asset>) {
        this.assets = assets
    }

    fun add(asset: Asset): Assets {
        if(assets.any { it.name == asset.name }) {
            throw RuntimeException("Asset with name ${asset.name} already exists")
        }
        return Assets(assets.plus(asset))
    }

}
