package org.ledgers.controllers

import org.ledgers.domain.Story
import org.ledgers.domain.StoryId
import org.ledgers.infrastructure.StoryRepositoryAdapter
import org.ledgers.presentation.StoryPage
import org.ledgers.usecase.GetStoryUseCase
import org.ledgers.usecase.SaveStoryUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StoryPageController(
    val storyRepository: StoryRepositoryAdapter
) {

    @RequestMapping("/story/new", produces = [MediaType.TEXT_HTML_VALUE])
    fun renderStory(): ResponseEntity<String> {
        val story = SaveStoryUseCase(storyRepository).saveStory(Story.create())
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header("Location", "/story/${story.id}").build()
    }

    @RequestMapping("/story/{id}", produces = [MediaType.TEXT_HTML_VALUE])
    fun renderStory(@PathVariable("id") id: String): String {
        val story = GetStoryUseCase(storyRepository).findStoryById(StoryId(id))
        return StoryPage(id, story).toHtml()
    }



}
