package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Assets(val assets: List<Asset> = emptyList()) {

    val numberOfAssets get() = assets.size
    val last get() = assets.last()

    fun add(asset: Asset): Assets {
        if (assets.any { it.name == asset.name }) {
            throw RuntimeException("Asset with name ${asset.name} already exists")
        }
        return Assets(assets.plus(asset))
    }

    fun remove(reference: ComponentReference): Assets {
        if (assets.none { it.reference == reference }) {
            throw RuntimeException("Asset with reference ${reference} not found")
        }
        return Assets(assets.filterNot { it.reference == reference  })
    }

    fun change(reference: ComponentReference, name: String, assetType: AssetType): Assets {
        return Assets(
            assets.map {
                if (reference == it.reference) {
                    Asset(it.id, it.version, name, assetType)
                } else {
                    it
                }
            }
        )
    }

    fun toArray(): Array<Asset> {
        return assets.toTypedArray()
    }

    fun getByReference(reference: ComponentReference): Asset {
        val asset = assets.find { it.reference == reference }
        return asset ?: throw RuntimeException("No asset with reference $reference")
    }

}
