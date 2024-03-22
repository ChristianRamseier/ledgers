package org.ledgers.dto

import org.ledgers.domain.Version
import org.ledgers.domain.architecture.OrganizationId

data class OrganizationDto(
    val id: OrganizationId,
    val version: Version,
    val name: String
){

}
