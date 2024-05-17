package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.stage.*
import kotlin.js.JsExport

@JsExport
data class Storyline(
    val chapters: List<Chapter> = listOf(Chapter.Empty)
) {

    val numberOfChapters get() = chapters.size

    fun withChangeInChapter(chapter: Int, change: StageChange): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withChange(change))
    }

    fun withoutChangeToComponentAtChapter(chapter: Int, reference: ComponentReference): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withoutChangesTo(reference))
    }

    fun withMaximumChapterBeing(chapter: Int): Storyline {
        val copy = ArrayList<Chapter>(chapter + 1)
        for (i in 0..chapter) {
            copy.add(atChapter(i))
        }
        return Storyline(copy)
    }

    fun atChapter(chapter: Int): Chapter {
        if (chapter >= 0 && chapter < chapters.size) {
            return chapters[chapter]
        }
        return Chapter.Empty
    }

    private fun withChangesInChapter(chapter: Int, updatedChapter: Chapter): Storyline {
        if (chapter < 0) {
            throw RuntimeException("Chapter must be greater than equal to 0")
        }
        val maxChapter = chapters.size.coerceAtLeast(chapter + 1)
        val copy = ArrayList<Chapter>(maxChapter)
        for (i in 0..<maxChapter) {
            copy.add(
                if (i == chapter) {
                    updatedChapter
                } else {
                    atChapter(i)
                }
            )
        }
        return Storyline(copy)
    }

    fun getStageAtChapter(chapter: Int): Stage {
        val componentsOnStage = LinkedHashMap<ComponentReference, ComponentOnStage>()
        for (i in 0..chapter) {
            val theChapter = atChapter(i)
            theChapter.changes.forEach { change ->
                when (change) {
                    is Add -> {
                        if (componentsOnStage.containsKey(change.componentReference)) {
                            throw RuntimeException("Stage already contains component ${change.componentReference}")
                        }
                        componentsOnStage[change.componentReference] = change.component
                    }

                    is Change -> {
                        if (!componentsOnStage.containsKey(change.componentReference)) {
                            throw RuntimeException("Stage does not contain component ${change.componentReference}")
                        }
                        componentsOnStage[change.componentReference] = change.component
                    }

                    is Remove -> {
                        if (!componentsOnStage.containsKey(change.componentReference)) {
                            throw RuntimeException("Stage does not contain component ${change.componentReference}")
                        }
                        componentsOnStage.remove(change.componentReference)
                    }
                }
            }
        }
        return Stage(
            components = componentsOnStage.values.toList()
        )
    }

    fun withChapterNamed(chapter: Int, name: String): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withName(name))
    }

    fun withLedgerInChapter(chapter: Int, reference: ComponentReference, box: Box): Storyline {
        return withComponentOnStageInChapter(chapter, LedgerOnStage(reference, box))
    }

    fun withLinkInChapter(chapter: Int, link: ComponentReference, from: Anchor, to: Anchor): Storyline {
        return withComponentOnStageInChapter(chapter, LinkOnStage(link, from, to))
    }

    /**
     * Figures out which type of change needs to be added and performs basic sanitizing after insertion.
     */
    fun withComponentOnStageInChapter(chapter: Int, component: ComponentOnStage): Storyline {
        val stageAtChapter = getStageAtChapter(chapter)
        val reference = component.reference
        if (!stageAtChapter.has(reference)) {
            // component does not appear on stage in current chapter
            val stageChange = atChapter(chapter).findStageChange(reference)
            if (stageChange is Remove) {
                // component was removed in this chapter -> replace removal with change
                return withChangeInChapter(chapter, Change(component))
            } else {
                // component is not present or was removed in a previous chapter -> add it
                val newStoryline = withChangeInChapter(chapter, Add(component))
                return newStoryline.removeFirstDoubleAddition(reference)
            }
        } else {
            // component appears on current stage
            val stageChange = atChapter(chapter).findStageChange(reference)
            if (stageChange is Add) {
                // component was added in current chapter -> change the addition to the new box
                return withChangeInChapter(chapter, Add(component))
            } else if (stageChange is Change || stageChange == null) {
                // component was changed in current or added in previous chapter -> add or replace the change
                return withChangeInChapter(chapter, Change(component))
            } else {
                throw RuntimeException("Unexpected stage change: $stageChange")
            }
        }
    }

    private fun removeFirstDoubleAddition(reference: ComponentReference): Storyline {
        var lastChangeType: StageChangeType? = null
        for (i in 0..<chapters.size) {
            val currentChapter = atChapter(i)
            currentChapter.changes.forEach { change ->
                if (change.componentReference == reference) {
                    if (lastChangeType == StageChangeType.Add && change is Add) {
                        return withChangeInChapter(i, Change(change.component))
                    }
                    lastChangeType = change.type
                }
            }
        }
        return this
    }

}
