package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ledgers.domain.stage.Anchor


@Serializable
@SerialName("LinkOnStage")
data class LinkOnStageDto(
    override val reference: ComponentReferenceDto,
    val fromAnchor: Anchor,
    val toAnchor: Anchor
) : ComponentOnStageDto
