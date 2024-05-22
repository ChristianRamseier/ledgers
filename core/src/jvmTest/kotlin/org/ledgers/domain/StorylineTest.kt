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
    val componentOnStage = LedgerOnStage(ComponentReference(ComponentType.Ledger, id, Version.Zero), Box(0, 0, 100, 10))
    val movedComponentOnStage = LedgerOnStage(ComponentReference(ComponentType.Ledger, id, Version.Zero), Box(100, 0, 100, 100))

    @Test
    fun givenAnEmptyTimeline_whenAddingAChange_theChangePrevailsInTime() {
        val storyline = Storyline()
        val updatedStoryline = storyline.withChangeInChapter(0, Add(componentOnStage))
        assertThat(updatedStoryline.chapters).containsExactly(Chapter(listOf(Add(componentOnStage))))
        assertThat(updatedStoryline.getStageAtChapter(0).components).containsExactly(componentOnStage)
        assertThat(updatedStoryline.getStageAtChapter(1).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenAnEmptyTimeline_whenGettingStageAtFutureTime_anEmptyStageIsReturned() {
        val storyline = Storyline()
        assertThat(storyline.getStageAtChapter(42).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenReplacingIt_anUpdatedStageIsReturned() {
        val storyline = Storyline().withChangeInChapter(0, Add(componentOnStage))
        val updatedStoryline = storyline.withChangeInChapter(0, Add(movedComponentOnStage))
        assertThat(updatedStoryline.chapters).containsExactly(Chapter(listOf(Add(movedComponentOnStage))))
        assertThat(updatedStoryline.getStageAtChapter(0).components).containsExactly(movedComponentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenRemovingIt_anEmptyStageIsReturned() {
        val storyline = Storyline().withChangeInChapter(0, Add(componentOnStage))
        val updatedStoryline = storyline.withoutChangeToComponentAtChapter(0, componentOnStage.reference)
        assertThat(updatedStoryline.getStageAtChapter(0).components).isEmpty()
    }

    @Test
    fun givenATimelineWithAChange_whenCuttingIt_anEmptyTimelineIsReturned() {
        val storyline = Storyline()
            .withChangeInChapter(0, Add(componentOnStage))
            .withChangeInChapter(1, Change(movedComponentOnStage))
        val updatedStoryline = storyline.withMaximumChapterBeing(0)
        assertThat(updatedStoryline.chapters).containsExactly(Chapter(listOf(Add(componentOnStage))))
        assertThat(updatedStoryline.getStageAtChapter(0).components).containsExactly(componentOnStage)
    }

    @Test
    fun givenATimelineWithAChange_whenAddingTheSameComponentAgainLater_anExceptionIsThrownWhenRequestingTheStage() {
        val storyline = Storyline()
            .withChangeInChapter(0, Add(componentOnStage))
            .withChangeInChapter(1, Add(movedComponentOnStage))
        assertThrows<RuntimeException> {
            storyline.getStageAtChapter(1)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenAddingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val storyline = Storyline()
            .withChangeInChapter(0, Change(componentOnStage))
        assertThrows<RuntimeException> {
            storyline.getStageAtChapter(0)
        }
    }

    @Test
    fun givenAnEmptyTimeline_whenRemovingAChangeBeforeAddition_anExceptionIsThrownWhenRequestingTheStage() {
        val storyline = Storyline()
            .withChangeInChapter(0, Remove(componentOnStage.reference))
        assertThrows<RuntimeException> {
            storyline.getStageAtChapter(0)
        }
    }

    @Test
    fun givenAnAddition_whenAddingAnotherAdditionBeforeIt_theLaterAdditionIsChangedToAChange() {
        val storyline = Storyline()
            .withChangeInChapter(1, Add(componentOnStage))
            .withComponentOnStageInChapter(0, movedComponentOnStage)

        assertThat(storyline.chapters).containsExactly(
            Chapter(listOf(Add(movedComponentOnStage))),
            Chapter(listOf(Change(componentOnStage))),
        )
    }

}
