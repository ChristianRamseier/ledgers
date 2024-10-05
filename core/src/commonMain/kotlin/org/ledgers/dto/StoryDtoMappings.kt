package org.ledgers.dto

import org.ledgers.domain.Chapter
import org.ledgers.domain.Story
import org.ledgers.domain.Storyline
import org.ledgers.domain.architecture.*
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import org.ledgers.domain.scenario.*
import org.ledgers.domain.scenario.action.*
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
    return ChapterDto(name = name, changes = changes.map { it.toDto() }, scenario = scenario.toDto())
}

private fun Scenario.toDto(): ScenarioDto {
    return ScenarioDto(steps = steps.steps.map { it.toDto() })
}

private fun Step.toDto(): StepDto {
    return StepDto(
        description = description,
        actions = actions.map { it.toDto() }
    )
}

private fun Action.toDto(): ActionDto {
    return when (this) {
        is CreateAccountAction -> this.toDto()
        is IssueAction -> this.toDto()
        is ReduceAction -> this.toDto()
        is TransferAction -> this.toDto()
    }
}

fun CreateAccountAction.toDto(): CreateAccountActionDto {
    return CreateAccountActionDto(
        id = id,
        ledgerId = ledgerId,
        accountReference = accountReference,
        owner = owner
    )
}

fun IssueAction.toDto(): IssueActionDto {
    return IssueActionDto(
        id = id,
        ledgerId = ledgerId,
        asset = asset,
        quantity = quantity,
        issueAccount = issueAccount,
        targetAccount = targetAccount
    )
}

fun ReduceAction.toDto(): ReduceActionDto {
    return ReduceActionDto(
        id = id,
        ledgerId = ledgerId,
        asset = asset,
        quantity = quantity,
        sourceAccount = sourceAccount,
        issueAccount = issueAccount,
    )
}

fun TransferAction.toDto(): TransferActionDto {
    return TransferActionDto(
        id = id,
        asset = asset,
        quantity = quantity,
        fromAccount = fromAccount.toDto(),
        toAccount = toAccount.toDto(),
        intermediateBookings = intermediateBookings.map { it.toDto() }
    )
}

private fun Booking.toDto(): BookingDto {
    return BookingDto(
        account = account.toDto(),
        quantity = quantity,
        asset = asset,
        type = type
    )
}

private fun LedgerAndAccountReference.toDto(): LedgerAndAccountReferenceDto {
    return LedgerAndAccountReferenceDto(ledgerId = ledgerId, accountReference = accountReference)
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
    return when (this) {
        is LedgerOnStage -> LedgerOnStageDto(reference.toDto(), box.toDto())
        is LinkOnStage -> LinkOnStageDto(reference.toDto(), fromAnchor, toAnchor)
    }
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
    return Chapter(
        name = name,
        changes = changes.map { it.toDomain() },
        scenario = scenario.toDomain()
    )
}

private fun ScenarioDto.toDomain(): Scenario {
    return Scenario(
        steps = Steps(steps.map { it.toDomain() })
    )
}

private fun StepDto.toDomain(): Step {
    return Step(
       description = description,
       actions = actions.map { it.toDomain() }
    )
}

private fun ActionDto.toDomain(): Action {
    return when(this) {
        is CreateAccountActionDto -> this.toDomain()
        is IssueActionDto -> this.toDomain()
        is ReduceActionDto -> this.toDomain()
        is TransferActionDto -> this.toDomain()
    }
}

private fun CreateAccountActionDto.toDomain(): CreateAccountAction {
    return CreateAccountAction(
        id = id,
        ledgerId = ledgerId,
        accountReference = accountReference,
        owner = owner
    )
}

private fun IssueActionDto.toDomain(): IssueAction {
    return IssueAction(
        id = id,
        ledgerId = ledgerId,
        asset = asset,
        quantity = quantity,
        issueAccount = issueAccount,
        targetAccount = targetAccount
    )
}

private fun ReduceActionDto.toDomain(): ReduceAction {
    return ReduceAction(
        id = id,
        ledgerId = ledgerId,
        asset = asset,
        quantity = quantity,
        sourceAccount = sourceAccount,
        issueAccount = issueAccount
    )
}

private fun TransferActionDto.toDomain(): TransferAction {
    return TransferAction(
        id = id,
        asset = asset,
        quantity = quantity,
        fromAccount = fromAccount.toDomain(),
        toAccount = toAccount.toDomain(),
        intermediateBookings = intermediateBookings.map { it.toDomain() }
    )
}

private fun BookingDto.toDomain(): Booking {
    return Booking(
        account = account.toDomain(),
        quantity = quantity,
        asset = asset,
        type = type
    )
}

private fun LedgerAndAccountReferenceDto.toDomain(): LedgerAndAccountReference {
    return LedgerAndAccountReference(ledgerId = ledgerId, accountReference = accountReference)
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
    return when (this) {
        is LedgerOnStageDto -> LedgerOnStage(reference.toDomain(), box.toDomain())
        is LinkOnStageDto -> LinkOnStage(reference.toDomain(), fromAnchor, toAnchor)
    }
}

private fun BoxDto.toDomain(): Box {
    return Box(x = x, y = y, width = width, height = height)
}
