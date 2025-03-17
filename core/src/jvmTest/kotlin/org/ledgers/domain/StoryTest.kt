package org.ledgers.domain

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.ledgers.domain.stage.*

class StoryTest {

    @Test
    fun givenAnEmptyStory_whenLinkingTwoLedgers_itSucceeds() {
        var story = Story.create()
        story = story.addOrganization("UBS")
        story = story.addLedger("L1", story.architecture.organizations.last.id)
        story = story.addLedger("L2", story.architecture.organizations.last.id)
        val firstLedger = story.architecture.ledgers.ledgers[0]
        val secondLedger = story.architecture.ledgers.ledgers[1]
        val from = AnchorReference(firstLedger.reference, Anchor.Top)
        val to = AnchorReference(secondLedger.reference, Anchor.Top)
        story = story.withLinkInChapter(0, from, to)
        val link = story.architecture.links.last
        assertThat(link.from).isEqualTo(firstLedger.id)
        assertThat(link.to).isEqualTo(secondLedger.id)
    }


    @Test
    fun givenALedgerNameChange_whenGettingTheStageChanges_theChangeIsThere() {
        var story = Story.create()
        story = story.addOrganization("UBS")
        story = story.addLedger("L1", story.architecture.organizations.last.id)
        story = story.withLedgerInChapter(0, story.architecture.ledgers.last.reference, Box.ZERO)
        val newStory = story.withChangedLedger(story.architecture.ledgers.last.reference, "New name", story.architecture.organizations.last.id)

        val changes = story.getStageChanges(0, newStory, 0)

        assertThat(changes.changes).containsExactly(Change(newStory.storyline.getStageAtChapter(0).components.get(0)))

    }

    @Test
    fun givenANewStory_whenGettingTheStageChanges_OnlyAdditonsArePresent() {
        var story = Story.create()
        story = story.addOrganization("UBS")
        story = story.addLedger("L1", story.architecture.organizations.last.id)
        story = story.withLedgerInChapter(0, story.architecture.ledgers.last.reference, Box.ZERO)

        val changes = Story.create().getStageChanges(0, story, 0)

        assertThat(changes.changes).containsExactly(Add(story.storyline.getStageAtChapter(0).components.get(0)))

    }

}
