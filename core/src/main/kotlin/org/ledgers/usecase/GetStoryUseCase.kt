package org.ledgers.usecase

import org.ledgers.domain.Story
import org.ledgers.domain.StoryId
import org.ledgers.domain.repository.StoryRepository

class GetStoryUseCase(
    private val storyRepository: StoryRepository
) {

    fun getStoryById(id: StoryId): Story {
        return storyRepository.findById(id) ?: throw RuntimeException("No story with id ${id}")
    }


    fun findStoryById(id: StoryId): Story? {
        return storyRepository.findById(id)
    }

    fun getStories(): List<Story> {
        return storyRepository.findAll()
    }

}
