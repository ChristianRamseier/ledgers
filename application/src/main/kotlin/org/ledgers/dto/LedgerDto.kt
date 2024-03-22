package org.ledgers.dto

import org.ledgers.domain.Version
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId

data class LedgerDto(
    val id: LedgerId,
    val version: Version,
    val name: String,
    val ownerId: OrganizationId
) {

}
