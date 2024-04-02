package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class Architecture(
    val organizations: Organizations = Organizations(),
    val ledgers: Ledgers = Ledgers(),
    val assets: Assets = Assets()
) {

    fun addOrganization(name: String): Architecture {
        val organization = Organization(OrganizationId.random(), Version.Zero, name)
        return Architecture(
            organizations = organizations.addOrReplace(organization),
            ledgers = ledgers,
            assets = assets
        )
    }

    fun removeOrganization(reference: ComponentReference): Architecture {
        return Architecture(
            organizations = organizations.remove(reference),
            ledgers = ledgers,
            assets = assets
        )
    }

    fun addLedger(name: String, ownerId: OrganizationId): Architecture {
        val ledger = Ledger(LedgerId.random(), Version.Zero, name, ownerId)
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.addOrReplace(ledger),
            assets = assets
        )
    }

    fun removeLedger(reference: ComponentReference): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.remove(reference),
            assets = assets
        )
    }

    fun addAsset(name: String, assetType: AssetType): Architecture {
        val asset = Asset(name, assetType)
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets.add(asset)
        )
    }

    fun removeAsset(name: String): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers,
            assets = assets.remove(name)
        )
    }

    fun getComponent(componentReference: ComponentReference): Component {
        return when(componentReference.type) {
            ComponentType.Organization -> organizations.getByReference(componentReference)
            ComponentType.Ledger -> ledgers.getByReference(componentReference)
        }
    }

}
