package org.ledgers.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.component.ComponentType
import org.ledgers.domain.stage.ComponentOnStage
import org.ledgers.domain.stage.Location
import org.ledgers.domain.stage.StageChange

class StorylineTest {

    val componentOnStage = ComponentOnStage(Location(0, 0), ComponentReference(ComponentType.Ledger, LedgerId.random(), Version.Zero))
    val movedComponentOnStage = ComponentOnStage(Location(100, 0), ComponentReference(ComponentType.Ledger, LedgerId.random(), Version.Zero))

    @Test
    fun givenAnEmptyTimeline_whenAddingAChangeAtTimeTwo_theChangeSucceeds() {
        val timeline = Storyline()
        val updatedTimeline = timeline.withChangeInChapter(0, StageChange.Add(componentOnStage))
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(StageChange.Add(componentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenAnEmptyTimeline_whenGettingStageAtFutureTime_anEmptyStageIsReturned() {
        val timeline = Storyline()
        assertThat(timeline.getStageAtChapter(42).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenReplacingIt_anUpdatedStageIsReturned() {
        val timeline = Storyline().withChangeInChapter(0, StageChange.Add(componentOnStage))
        val updatedTimeline = timeline.withChangeInChapter(0, StageChange.Add(movedComponentOnStage))
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(StageChange.Add(movedComponentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(movedComponentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenRemovingIt_anEmptyStageIsReturned() {
        val timeline = Storyline().withChangeInChapter(0, StageChange.Add(componentOnStage))
        val updatedTimeline = timeline.withoutChangeToComponentAtChapter(0, componentOnStage.reference)
        assertThat(updatedTimeline.getStageAtChapter(0).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenCuttingIt_anEmptyTimelineIsReturned() {
        val timeline = Storyline()
            .withChangeInChapter(0, StageChange.Add(componentOnStage))
            .withChangeInChapter(1, StageChange.Change(movedComponentOnStage))
        val updatedTimeline = timeline.withMaximumChapterBeing(0)
        assertThat(updatedTimeline.chapters).containsExactly(Chapter(listOf(StageChange.Add(componentOnStage))))
        assertThat(updatedTimeline.getStageAtChapter(0).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenAddingTheSameComponentAgainLater_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, StageChange.Add(componentOnStage))
            .withChangeInChapter(1, StageChange.Add(movedComponentOnStage))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(1)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenAddingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, StageChange.Change(componentOnStage))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(0)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenRemovingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val timeline = Storyline()
            .withChangeInChapter(0, StageChange.Remove(componentOnStage.reference))
        assertThrows<RuntimeException> {
            timeline.getStageAtChapter(0)
        }
    }

}
