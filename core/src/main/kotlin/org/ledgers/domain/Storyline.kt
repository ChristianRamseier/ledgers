package org.ledgers.domain

import org.ledgers.domain.component.ComponentReference

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

    private fun withChangesInChapter(chapter: Int, changes: Chapter): Storyline {
        if (chapter < 0) {
            throw RuntimeException("Chapter must be greater than equal to 0")
        }
        val maxChapter = chapters.size.coerceAtLeast(chapter + 1)
        val copy = ArrayList<Chapter>(maxChapter)
        for (i in 0..<maxChapter) {
            copy.add(
                if (i == chapter) {
                    changes
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
            val changes = atChapter(chapter)
            changes.changes.forEach { change ->
                when (change) {
                    is StageChange.Add -> {
                        if (componentsOnStage.containsKey(change.componentReference)) {
                            throw RuntimeException("Stage already contains component ${change.componentReference}")
                        }
                        componentsOnStage[change.componentReference] = change.component
                    }

                    is StageChange.Change -> {
                        if (!componentsOnStage.containsKey(change.componentReference)) {
                            throw RuntimeException("Stage does not contain component ${change.componentReference}")
                        }
                        componentsOnStage[change.componentReference] = change.component
                    }

                    is StageChange.Remove -> {
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

}
