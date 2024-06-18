package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.scenario.action.ActionType

@Serializable
sealed interface ActionDto {

    val id: ActionId
    val type: ActionType
}
