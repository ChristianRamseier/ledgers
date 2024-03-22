package org.ledgers.presentation

import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import org.ledgers.domain.Story

class StoriesPage(private val stories: List<Story>) : Page {

    override fun toHtml(): String {
        return Site.getHtmlFor("Stories") {
            h1 { +"Stories" }
            ul {
                stories.forEach { story ->
                    li {
                        a(href = "/story/${story.id}") { +"${story.getDisplayName()}" }
                    }
                }
            }
        }
    }

}
