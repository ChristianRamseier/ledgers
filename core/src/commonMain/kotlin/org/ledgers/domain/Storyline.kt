package org.ledgers.domain

import org.ledgers.domain.architecture.Ledger
import org.ledgers.domain.architecture.Link
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.scenario.Step
import org.ledgers.domain.scenario.action.Action
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.stage.*
import org.ledgers.replaceAtIndex
import kotlin.js.JsExport

@JsExport
data class Storyline(
    val chapters: List<Chapter> = listOf(Chapter.Empty)
) {

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
        val updatedChapters = chapters.replaceAtIndex(chapter, updatedChapter, Chapter.Empty)
        return Storyline(updatedChapters)
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


    fun withoutLedgerAppearance(chapter: Int, ledgerReference: Ledger, links: List<Link>): Storyline {
        return removeComponentAppearance(chapter, ledgerReference.reference, links)
    }

    fun withoutLinkAppearance(chapter: Int, link: Link): Storyline {
        return removeComponentAppearance(chapter, link.reference, emptyList())
    }

    private fun removeComponentAppearance(
        chapter: Int,
        componentToRemove: ComponentReference,
        additionalLinksToRemove: List<Link>
    ): Storyline {
        val stageAtChapter = getStageAtChapter(chapter)
        if (stageAtChapter.has(componentToRemove)) {
            val chapterWhereComponentWasAdded = findAdditionBackwardsFromChapter(chapter, componentToRemove)
            val chapterWhereComponentIsRemoved = findRemovalForwardFromChapter(chapter, componentToRemove)
            var storyline = this
            (chapterWhereComponentWasAdded..chapterWhereComponentIsRemoved).forEach { i ->
                storyline = storyline.withoutChangeToComponentAtChapter(i, componentToRemove)
                additionalLinksToRemove.forEach { link ->
                    storyline = storyline.withoutChangeToComponentAtChapter(i, link.reference)
                }
            }
            return storyline
        } else {
            throw RuntimeException("Cannot remove component $componentToRemove because it is not on stage in chapter $chapter")
        }
    }

    private fun findRemovalForwardFromChapter(chapter: Int, reference: ComponentReference): Int {
        if (chapter == chapters.size - 1) {
            return chapter // last chapter
        } else if (atChapter(chapter).findStageChange(reference) is Remove) {
            return chapter // chapter where component is removed
        } else {
            return findRemovalForwardFromChapter(chapter + 1, reference)
        }
    }

    private fun findAdditionBackwardsFromChapter(chapter: Int, reference: ComponentReference): Int {
        if (atChapter(chapter).findStageChange(reference) is Add) {
            return chapter
        } else if (chapter > 0) {
            return findAdditionBackwardsFromChapter(chapter - 1, reference)
        } else {
            throw RuntimeException("Unable to find addition for $reference when searching backwards.")
        }
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

    fun withNewScenarioStep(chapter: Int, step: Step, insertAt: Int = -1): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withNewScenarioStep(step, insertAt))
    }

    fun withoutScenarioStep(chapter: Int, step: Int): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withoutScenarioStep(step))
    }

    fun withActionAtScenarioStep(chapter: Int, action: Action, step: Int): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withActionAtScenarioStep(action, step))
    }

    fun withoutActionAtStep(chapter: Int, step: Int, actionId: ActionId): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withoutActionAtStep(actionId, step))
    }

    fun withDescriptionAtStep(chapter: Int, step: Int, description: String): Storyline {
        return withChangesInChapter(chapter, atChapter(chapter).withDescriptionAtStep(description, step))
    }

    fun isComponentUsed(reference: ComponentReference): Boolean {
        return chapters.any { it.isComponentUsed(reference) }
    }

}
