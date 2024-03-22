package org.ledgers.dto

import org.ledgers.domain.*
import org.ledgers.domain.architecture.*
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Scenario
import org.ledgers.domain.scenario.Steps

fun Story.toDto(): StoryDto {
    return StoryDto(
        id = id,
        version = version,
        name = name,
        architecture = architecture.toDto(),
        storyline = storyline.toDto()
    )
}

private fun Architecture.toDto(): ArchitectureDto {
    return ArchitectureDto(
        organizations = organizations.organizations.map { it.toDto() },
        ledgers = ledgers.ledgers.map { it.toDto() },
        assets = assets.assets.map { it.toDto() }
    )
}

private fun Asset.toDto(): AssetDto {
    return AssetDto(name = name, assetType = assetType)
}

private fun Ledger.toDto(): LedgerDto {
    return LedgerDto(id = id, version = version, name = name, ownerId = ownerId)
}

private fun Organization.toDto(): OrganizationDto {
    return OrganizationDto(id = id, version = version, name = name)
}

private fun Storyline.toDto(): StorylineDto {
    return StorylineDto(chapters.map { it.toDto() })
}

private fun Chapter.toDto(): ChapterDto {
    return ChapterDto(name = name, changes = changes.map { it.toDto() }, scenario = scenario?.toDto())
}

private fun Scenario.toDto(): ScenarioDto {
    return ScenarioDto(name = name)
}

private fun StageChange.toDto(): StageChangeDto {
    return when (this) {
        is StageChange.Add -> StageChangeDto.Add(component.toDto())
        is StageChange.Change -> StageChangeDto.Change(component.toDto())
        is StageChange.Remove -> StageChangeDto.Remove(componentReference.toDto())
    }
}

private fun ComponentReference.toDto(): ComponentReferenceDto {
    return ComponentReferenceDto(type = type, id = id, version = version)
}

private fun ComponentOnStage.toDto(): ComponentOnStageDto {
    return ComponentOnStageDto(location = location.toDto(), reference = reference.toDto())
}

private fun Location.toDto(): LocationDto {
    return LocationDto(x = x, y = y)
}


fun StoryDto.toDomain(): Story {
    return Story(
        id = id,
        version = version,
        name = name,
        architecture = architecture.toDomain(),
        storyline = storyline.toDomain()
    )
}

private fun ArchitectureDto.toDomain(): Architecture {
    return Architecture(
        organizations = Organizations(organizations.map { it.toDomain() }),
        ledgers = Ledgers(ledgers.map { it.toDomain() }),
        assets = Assets(assets.map { it.toDomain() })
    )
}

private fun AssetDto.toDomain(): Asset {
    return Asset(name = name, assetType = assetType)
}

private fun LedgerDto.toDomain(): Ledger {
    return Ledger(id = id, version = version, name = name, ownerId = ownerId)
}

private fun OrganizationDto.toDomain(): Organization {
    return Organization(id = id, version = version, name = name)
}

private fun StorylineDto.toDomain(): Storyline {
    return Storyline(chapters.map { it.toDomain() })
}

private fun ChapterDto.toDomain(): Chapter {
    return Chapter(name = name, changes = changes.map { it.toDomain() }, scenario = scenario?.toDomain())
}

private fun ScenarioDto.toDomain(): Scenario {
    return Scenario(name = name, steps = Steps(emptyList()))
}

private fun StageChangeDto.toDomain(): StageChange {
    return when (this) {
        is StageChangeDto.Add -> StageChange.Add(component.toDomain())
        is StageChangeDto.Change -> StageChange.Change(component.toDomain())
        is StageChangeDto.Remove -> StageChange.Remove(componentReference.toDomain())
    }
}

private fun ComponentReferenceDto.toDomain(): ComponentReference {
    return ComponentReference(type = type, id = id, version = version)
}

private fun ComponentOnStageDto.toDomain(): ComponentOnStage {
    return ComponentOnStage(location = location.toDomain(), reference = reference.toDomain())
}

private fun LocationDto.toDomain(): Location {
    return Location(x = x, y = y)
}
