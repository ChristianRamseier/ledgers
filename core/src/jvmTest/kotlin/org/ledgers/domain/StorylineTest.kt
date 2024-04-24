package org.ledgers.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.component.ComponentType
import org.ledgers.domain.stage.*


class StorylineTest {

    val id = LedgerId.random()
    val componentOnStage = ComponentOnStage(Box(0, 0, 100, 10), ComponentReference(ComponentType.Ledger, id, Version.Zero))
    val movedComponentOnStage = ComponentOnStage(Box(100, 0, 100, 100), ComponentReference(ComponentType.Ledger, id, Version.Zero))

    @Test
    fun givenAnEmptyTimeline_whenAddingAChange_theChangePrevailsInTime() {
        val timeline = Storyline()
        val updatedTimeline = timeline.withChangeInChapter(0, Add(componentOnStage))
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(Add(componentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(componentOnStage)
        assertThat(updatedTimeline.getStageAtChapter(1).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenAnEmptyTimeline_whenGettingStageAtFutureTime_anEmptyStageIsReturned() {
        val timeline = Storyline()
        assertThat(timeline.getStageAtChapter(42).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenReplacingIt_anUpdatedStageIsReturned() {
        val timeline = Storyline().withChangeInChapter(0, Add(componentOnStage))
        val updatedTimeline = timeline.withChangeInChapter(0, Add(movedComponentOnStage))
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(Add(movedComponentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(movedComponentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenRemovingIt_anEmptyStageIsReturned() {
        val timeline = Storyline().withChangeInChapter(0, Add(componentOnStage))
        val updatedTimeline = timeline.withoutChangeToComponentAtChapter(0, componentOnStage.reference)
        assertThat(updatedTimeline.getStageAtChapter(0).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenCuttingIt_anEmptyTimelineIsReturned() {
        val timeline = Storyline()
            .withChangeInChapter(0, Add(componentOnStage))
            .withChangeInChapter(1, Change(movedComponentOnStage))
        val updatedTimeline = timeline.withMaximumChapterBeing(0)
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(Add(componentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenAddingTheSameComponentAgainLater_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, Add(componentOnStage))
            .withChangeInChapter(1, Add(movedComponentOnStage))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(1)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenAddingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, Change(componentOnStage))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(0)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenRemovingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, Remove(componentOnStage.reference))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(0)
        }
    }

}
