package org.ledgers.dto

sealed interface StageChangeDto {

    val componentReference: ComponentReferenceDto

    data class Add(
        val component: ComponentOnStageDto
    ) : StageChangeDto {
        override val componentReference: ComponentReferenceDto get() = component.reference
    }

    data class Change(
        val component: ComponentOnStageDto
    ) : StageChangeDto {
        override val componentReference: ComponentReferenceDto get() = component.reference
    }

    data class Remove(
        override val componentReference: ComponentReferenceDto
    ) : StageChangeDto
}
