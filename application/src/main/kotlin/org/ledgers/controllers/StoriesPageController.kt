package org.ledgers.controllers

import org.ledgers.infrastructure.StoryRepositoryAdapter
import org.ledgers.presentation.StoriesPage
import org.ledgers.usecase.GetStoryUseCase
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StoriesPageController(
    val storyRepository: StoryRepositoryAdapter
) {


    @RequestMapping("/", produces = [MediaType.TEXT_HTML_VALUE])
    fun renderHomePage(): String {
        val stories = GetStoryUseCase(storyRepository).getStories()
        return StoriesPage(stories).toHtml()
    }


}
