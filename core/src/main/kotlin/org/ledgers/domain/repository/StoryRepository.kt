package org.ledgers.domain.repository

import org.ledgers.domain.Story
import org.ledgers.domain.StoryId

interface StoryRepository {

    fun save(story: Story): Story

    fun findAll(): List<Story>

    fun findById(storyId: StoryId): Story?

}
