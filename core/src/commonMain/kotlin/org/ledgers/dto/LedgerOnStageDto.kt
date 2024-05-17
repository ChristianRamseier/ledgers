package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
@SerialName("LedgerOnStage")
data class LedgerOnStageDto(
    override val reference: ComponentReferenceDto,
    val box: BoxDto
) : ComponentOnStageDto
