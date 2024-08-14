package org.ledgers.domain

import org.ledgers.domain.architecture.*
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Step
import org.ledgers.domain.stage.AnchorReference
import org.ledgers.domain.stage.Box
import org.ledgers.domain.stage.StageChange
import kotlin.js.JsExport

@JsExport
data class Story(
    val id: StoryId,
    val name: String,
    val architecture: Architecture,
    val storyline: Storyline
) {

    fun getDisplayName(): String {
        if (name.isNullOrBlank()) {
            return "Story #${id}"
        }
        return name
    }

    fun getComponent(componentReference: ComponentReference): Component {
        return architecture.getComponent(componentReference)
    }

    fun getComponentDisplayName(componentReference: ComponentReference, chapter: Int): String {
        val component = getComponent(componentReference)
        return when (component) {
            is Asset -> component.name
            is Ledger -> component.name
            is Organization -> component.name
            is Link -> {
                val from = getComponentDisplayName(storyline.getStageAtChapter(chapter).getById(component.from).reference, chapter)
                val to = getComponentDisplayName(storyline.getStageAtChapter(chapter).getById(component.to).reference, chapter)
                return "$from to $to"
            }
            else -> componentReference.toString()
        }
    }

    fun withName(name: String): Story {
        return copy(name = name)
    }

    fun addOrganization(name: String): Story {
        return copy(architecture = architecture.addOrganization(name))
    }

    fun removeOrganization(reference: ComponentReference): Story {
        return copy(architecture = architecture.removeOrganization(reference))
    }

    fun addLedger(name: String, ownerId: OrganizationId): Story {
        return copy(architecture = architecture.addLedger(name, ownerId))
    }

    fun removeLedger(reference: ComponentReference): Story {
        return copy(architecture = architecture.removeLedger(reference))
    }

    fun addAsset(name: String, assetType: AssetType = AssetType.Security): Story {
        return copy(architecture = architecture.addAsset(name, assetType))
    }

    fun withChangedAsset(reference: ComponentReference, name: String, assetType: AssetType): Story {
        return copy(architecture = architecture.withChangedAsset(reference, name, assetType))
    }

    fun removeAsset(name: String): Story {
        return copy(architecture = architecture.removeAsset(name))
    }

    fun withChangeInChapter(chapter: Int, change: StageChange): Story {
        return copy(storyline = storyline.withChangeInChapter(chapter, change))
    }

    fun withLedgerInChapter(chapter: Int, reference: ComponentReference, box: Box): Story {
        return copy(storyline = storyline.withLedgerInChapter(chapter, reference, box))
    }

    fun withLinkBetween(from: LedgerId, to: LedgerId): Story {
        return copy(architecture = architecture.addLinkIfNotExists(from, to))
    }

    fun withLinkInChapter(chapter: Int, from: AnchorReference, to: AnchorReference): Story {
        val story = withLinkBetween(from.ledgerId, to.ledgerId)
        val link = story.architecture.links.findBetween(from.ledgerId, to.ledgerId) ?: throw RuntimeException("No link between ${from.ledgerId} and ${to.ledgerId}")
        val updated = story.copy(storyline = storyline.withLinkInChapter(chapter, link.reference, from.anchor, to.anchor))
        val linkBack = updated.architecture.links.findBetween(to.ledgerId, from.ledgerId)
        return if (linkBack != null && updated.storyline.getStageAtChapter(chapter).has(linkBack.reference)) {
            updated.copy(storyline = updated.storyline.withLinkInChapter(chapter, linkBack.reference, to.anchor, from.anchor))
        } else {
            updated
        }
    }

    fun withChapterNamed(chapter: Int, name: String): Story {
        return copy(storyline = storyline.withChapterNamed(chapter, name))
    }

    fun withChangedLedger(reference: ComponentReference, name: String, ownerId: OrganizationId): Story {
        return copy(architecture = architecture.withChangedLedger(reference, name, ownerId))
    }

    fun withChangedOrganization(reference: ComponentReference, name: String): Story {
        return copy(architecture = architecture.withChangedOrganization(reference, name))
    }

    companion object {
        fun new(): Story {
            return Story(
                id = StoryId.random(),
                name = "",
                architecture = Architecture.Empty,
                storyline = Storyline()
            )
        }
    }
}
