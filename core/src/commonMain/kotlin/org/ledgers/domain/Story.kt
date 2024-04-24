package org.ledgers.domain

import org.ledgers.domain.architecture.Architecture
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.domain.component.Component
import org.ledgers.domain.component.ComponentReference
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

    fun withComponentInChapter(chapter: Int, reference: ComponentReference, box: Box): Story {
        return copy(storyline = storyline.withComponentInChapter(chapter, reference, box))
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
                architecture = Architecture(),
                storyline = Storyline()
            )
        }
    }
}
