package org.ledgers.usecase

import org.ledgers.domain.Story
import org.ledgers.domain.StoryId
import org.ledgers.domain.repository.StoryRepository

class SaveStoryUseCase(
    private val storyRepository: StoryRepository
) {

    fun saveStory(story: Story): Story {
        return storyRepository.save(story)
    }

}
