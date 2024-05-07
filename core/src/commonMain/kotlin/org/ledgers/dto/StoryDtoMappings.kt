package org.ledgers.dto

import org.ledgers.domain.Chapter
import org.ledgers.domain.Story
import org.ledgers.domain.Storyline
import org.ledgers.domain.architecture.*
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import org.ledgers.domain.scenario.Scenario
import org.ledgers.domain.scenario.Steps
import org.ledgers.domain.stage.*

fun Story.toDto(): StoryDto {
    return StoryDto(
        id = id,
        name = name,
        architecture = architecture.toDto(),
        storyline = storyline.toDto()
    )
}

private fun Architecture.toDto(): ArchitectureDto {
    return ArchitectureDto(
        organizations = organizations.organizations.map { it.toDto() },
        ledgers = ledgers.ledgers.map { it.toDto() },
        assets = assets.assets.map { it.toDto() },
        links = links.links.map { it.toDto() }
    )
}

private fun Link.toDto(): LinkDto {
    return LinkDto(id = id, version = version, from = from, to = to)
}

private fun Asset.toDto(): AssetDto {
    return AssetDto(id = id, version = version, name = name, assetType = assetType)
}

private fun Ledger.toDto(): LedgerDto {
    return LedgerDto(id = id, version = version, name = name, ownerId = ownerId, capabilities = capabilities.toDto())
}

private fun LedgerCapabilities.toDto(): List<LedgerCapabilityDto> {
    return capabilities.map { it.toDto() }
}

private fun LedgerCapability.toDto(): LedgerCapabilityDto {
    return LedgerCapabilityDto(
        assetId = assetId,
        sourceLedgerId = sourceLedgerId,
        accounting = accounting,
        sourceLabel = sourceLabel
    )
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
        is Add -> AddDto(component.toDto())
        is Change -> ChangeDto(component.toDto())
        is Remove -> RemoveDto(componentReference.toDto())
    }
}

private fun ComponentReference.toDto(): ComponentReferenceDto {
    return ComponentReferenceDto(type = type, id = id.id, version = version)
}

private fun ComponentOnStage.toDto(): ComponentOnStageDto {
    return ComponentOnStageDto(location = box.toDto(), reference = reference.toDto())
}

private fun Box.toDto(): BoxDto {
    return BoxDto(x = x, y = y, width = width, height = height)
}


fun StoryDto.toDomain(): Story {
    return Story(
        id = id,
        name = name,
        architecture = architecture.toDomain(),
        storyline = storyline.toDomain()
    )
}

private fun ArchitectureDto.toDomain(): Architecture {
    return Architecture(
        organizations = Organizations(organizations.map { it.toDomain() }),
        ledgers = Ledgers(ledgers.map { it.toDomain() }),
        assets = Assets(assets.map { it.toDomain() }),
        links = Links(links.map { it.toDomain() })
    )
}

private fun LinkDto.toDomain(): Link {
    return Link(id = id, version = version, from = from, to = to)
}

private fun AssetDto.toDomain(): Asset {
    return Asset(id = id, version = version, name = name, assetType = assetType)
}

private fun LedgerDto.toDomain(): Ledger {
    return Ledger(id = id, version = version, name = name, ownerId = ownerId, capabilities = LedgerCapabilities(capabilities.map { it.toDomain() }))
}

private fun LedgerCapabilityDto.toDomain(): LedgerCapability {
    return LedgerCapability(assetId = assetId, sourceLedgerId = sourceLedgerId, accounting = accounting, sourceLabel = sourceLabel)
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
        is AddDto -> Add(component.toDomain())
        is ChangeDto -> Change(component.toDomain())
        is RemoveDto -> Remove(componentReference.toDomain())
    }
}

private fun ComponentReferenceDto.toDomain(): ComponentReference {
    val componentId = when (type) {
        ComponentType.Ledger -> LedgerId(id)
        ComponentType.Organization -> OrganizationId(id)
        ComponentType.Asset -> AssetId(id)
        ComponentType.Link -> LinkId(id)
    }
    return ComponentReference(type = type, id = componentId, version = version)
}

private fun ComponentOnStageDto.toDomain(): ComponentOnStage {
    return ComponentOnStage(box = location.toDomain(), reference = reference.toDomain())
}

private fun BoxDto.toDomain(): Box {
    return Box(x = x, y = y, width = width, height = height)
}
