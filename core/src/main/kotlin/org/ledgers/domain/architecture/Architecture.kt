package org.ledgers.domain.architecture

import org.ledgers.domain.AssetType
import org.ledgers.domain.Version

class Architecture {

    val organizations: Organizations
    val ledgers: Ledgers
    val assets: Assets

    constructor(
        organizations: Organizations,
        ledgers: Ledgers,
        assets: Assets,
    ) {
        this.organizations = organizations
        this.ledgers = ledgers
        this.assets = assets
    }

    constructor() {
        this.organizations = Organizations()
        this.ledgers = Ledgers()
        this.assets = Assets()
    }

    fun addOrganization(name: String): Architecture {
        val organization = Organization(OrganizationId.nextId(), Version.Zero, name)
        return Architecture(
            organizations = organizations.add(organization),
            ledgers = ledgers,
            assets = assets
        )
    }

    fun removeOrganization(organizationId: OrganizationId): Architecture {
        return Architecture(
            organizations = organizations.remove(organizationId),
            ledgers = ledgers,
            assets = assets
        )
    }

    fun addLedger(name: String, ownerId: OrganizationId): Architecture {
        val ledger = Ledger(LedgerId.nextId(), Version.Zero, name, ownerId)
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.add(ledger),
            assets = assets
        )
    }

    fun removeLedger(ledgerId: LedgerId): Architecture {
        return Architecture(
            organizations = organizations,
            ledgers = ledgers.remove(ledgerId),
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

}
