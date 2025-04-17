package org.ledgers.domain.component

import org.ledgers.domain.Version
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.LinkId
import org.ledgers.domain.architecture.OrganizationId
import kotlin.js.JsExport

@JsExport
data class ComponentReference(
    val type: ComponentType,
    val id: ComponentId,
    val version: Version
) {

    override fun toString(): String {
        return "$type:${id.id}:$version"
    }

    companion object {

        fun fromString(string: String): ComponentReference {
            val parts = string.split(":")
            val type = ComponentType.valueOf(parts[0])
            val id = when (type) {
                ComponentType.Ledger -> LedgerId(parts[1])
                ComponentType.Organization -> OrganizationId(parts[1])
                ComponentType.Asset -> AssetId(parts[1])
                ComponentType.Link -> LinkId(parts[1])
            }
            val version = Version(parts[2].toInt())
            return ComponentReference(type, id, version)
        }

        fun fromReferenceParts(type: String, id: String, version: Int): ComponentReference {
            return fromString("$type:$id:$version")
        }

        fun forLedger(id: String, version: Int): ComponentReference {
            return ComponentReference(ComponentType.Ledger, LedgerId(id), Version(version))
        }


        fun forAsset(id: String, version: Int): ComponentReference {
            return ComponentReference(ComponentType.Asset, AssetId(id), Version(version))
        }


        fun forOrganization(id: String, version: Int): ComponentReference {
            return ComponentReference(ComponentType.Organization, OrganizationId(id), Version(version))
        }


        fun forLink(id: String, version: Int): ComponentReference {
            return ComponentReference(ComponentType.Link, LinkId(id), Version(version))
        }

    }
}
