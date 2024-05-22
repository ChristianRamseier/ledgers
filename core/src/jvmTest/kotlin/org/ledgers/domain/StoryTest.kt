package org.ledgers.domain

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.ledgers.domain.stage.Anchor
import org.ledgers.domain.stage.AnchorReference

class StoryTest {

    @Test
    fun givenAnEmptyStory_whenLinkingTwoLedgers_itSucceeds() {

        var story = Story.new()
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

}
