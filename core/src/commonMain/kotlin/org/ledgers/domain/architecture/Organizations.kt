package org.ledgers.domain.architecture

data class Organizations(val organizations: List<Organization> = emptyList()) {

    fun getById(id: OrganizationId): Organization {
        val organization = organizations.find { it.id == id }
        return organization ?: throw RuntimeException("No organization with $id")
    }

    fun add(organization: Organization): Organizations {
        return Organizations(organizations.plus(organization))
    }

    fun remove(id: OrganizationId): Organizations {
        val toRemove = getById(id)
        return Organizations(
            organizations.filter { it == toRemove }
        )
    }

}
