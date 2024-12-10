package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Organizations(
    val organizations: List<Organization> = emptyList()
) {

    val numberOfOrganizations get() = organizations.size
    val last get() = organizations.last()

    fun getByReference(reference: ComponentReference): Organization {
        val organization = organizations.find { it.reference == reference }
        return organization ?: throw RuntimeException("No organization with reference $reference")
    }

    fun addOrReplace(organization: Organization): Organizations {
        return Organizations(organizations.filter { it.reference != organization.reference }.plus(organization))
    }

    fun toArray(): Array<Organization> {
        return organizations.toTypedArray()
    }

    fun remove(id: ComponentReference): Organizations {
        val toRemove = getByReference(id)
        return Organizations(
            organizations.filter { it != toRemove }
        )
    }

    fun withChangedOrganization(reference: ComponentReference, name: String): Organizations {
        return Organizations(
            organizations.map {
                if (it.reference == reference) {
                    it.copy(name = name)
                } else {
                    it
                }
            }
        )
    }


}
