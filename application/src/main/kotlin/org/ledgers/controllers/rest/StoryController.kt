package org.ledgers.controllers.rest

import org.ledgers.domain.StoryId
import org.ledgers.dto.StoryDto
import org.ledgers.dto.toDomain
import org.ledgers.dto.toDto
import org.ledgers.infrastructure.StoryRepositoryAdapter
import org.ledgers.usecase.GetStoryUseCase
import org.ledgers.usecase.SaveStoryUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StoryController {

    @Autowired
    lateinit var storyRepository: StoryRepositoryAdapter


    @GetMapping("/api/story/{id}")
    fun getStoryById(@PathVariable id: StoryId): StoryDto {
        return GetStoryUseCase(storyRepository).getStoryById(id).toDto()
    }

    @PostMapping("/api/story/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveStory(@PathVariable id: StoryId, @RequestBody story: StoryDto): StoryDto {
        return SaveStoryUseCase(storyRepository).saveStory(story.toDomain()).toDto()
    }

}
