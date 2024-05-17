package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Architecture(
    val organizations: Organizations,
    val ledgers: Ledgers,
    val assets: Assets,
    val links: Links
) {

    fun addOrganization(name: String): Architecture {
        val organization = Organization(OrganizationId.random(), Version.Zero, name)
        return Architecture(
            organizations = organizations.addOrReplace(organization),
            ledgers = ledgers,
            assets = assets,
            links = links
        )
    }

    fun removeOrganization(reference: ComponentReference): Architecture {
        return Architecture(
            organizations = organizations.remove(reference),
            ledgers = ledgers,
            assets = assets,
            links = links
        )
    }

    fun addLedger(name: String, ownerId: OrganizationId): Architecture {
        val ledger = Ledger(LedgerId.random(), Version.Zero, name, ownerId, LedgerCapabilities.None)
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.addOrReplace(ledger),
            assets = assets,
            links = links
        )
    }

    fun withChangedLedger(reference: ComponentReference, name: String, ownerId: OrganizationId): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.withChangedLedger(reference, name, ownerId),
            assets = assets,
            links = links
        )
    }

    fun withChangedOrganization(reference: ComponentReference, name: String): Architecture {
        return Architecture(
            organizations = organizations.withChangedOrganization(reference, name),
            ledgers = ledgers,
            assets = assets,
            links = links
        )
    }

    fun removeLedger(reference: ComponentReference): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.remove(reference),
            assets = assets,
            links = links
        )
    }

    fun addAsset(name: String, assetType: AssetType): Architecture {
        val asset = Asset(AssetId.random(), Version.Zero, name, assetType)
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets.add(asset),
            links = links
        )
    }

    fun withChangedAsset(reference: ComponentReference, name: String, assetType: AssetType): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets.change(reference, name, assetType),
            links = links
        )
    }

    fun removeAsset(name: String): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets.remove(name),
            links = links
        )
    }

    fun addLinkIfNotExists(from: LedgerId, to: LedgerId): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets,
            links = links.addLinkIfNotExists(from, to)
        )
    }

    fun removeLink(reference: ComponentReference): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets,
            links = links.remove(reference)
        )
    }

    fun withChangedLink(reference: ComponentReference, from: LedgerId, to: LedgerId): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets,
            links = links.change(reference, from, to)
        )
    }

    fun getComponent(componentReference: ComponentReference): Component {
        return when (componentReference.type) {
            ComponentType.Organization -> organizations.getByReference(componentReference)
            ComponentType.Ledger -> ledgers.getByReference(componentReference)
            ComponentType.Asset -> assets.getByReference(componentReference)
            ComponentType.Link -> links.getByReference(componentReference)
        }
    }


    companion object {
        val Empty = Architecture(
            organizations = Organizations(),
            ledgers = Ledgers(),
            assets = Assets(),
            links = Links()
        )
    }


}
