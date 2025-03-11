package org.ledgers.presentation

import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.svg
import org.ledgers.domain.Story
import org.ledgers.presentation.components.storyElement

class StoryPage(private val storyId: String, private val story: Story?) : Page {

    override fun toHtml(): String {
        if (story == null) {
            return Site.getHtmlFor("Story not found") {
                +"No story found by id $storyId"
            }
        }
        return Site.getHtmlFor("${story.getDisplayName()} - Story") {
            div {
                id = "story-id"
                attributes["story-id"] = storyId
            }
            div {
                id = "container"
                div {
                    id = "canvas-container"
                    svg {
                        id = "canvas-edges-svg"
                        defs {
                            marker {
                                id = "arrowhead"
                                attributes["markerWidth"] = "5"
                                attributes["markerHeight"] = "4"
                                attributes["refX"] = "2.5"
                                attributes["refY"] = "2"
                                attributes["orient"] = "auto"
                                polygon { attributes["points"] = "0 0, 5 2, 0 4" }
                            }
                        }
                        g { id = "canvas-edge-paths" }
                        g { id = "canvas-edge-link-hint" }
                    }

                    div {
                        id = "canvas-edges"

                    }

                    div {
                        id = "canvas-nodes"
                    }

                }
                div {
                    id = "components"
                }
                div {
                    id = "controls"
                    div {
                        id = "zoom-controls"
                        button {
                            id = "zoom-out"
                            +"Zoom out"
                        }
                        button {
                            id = "zoom-in"
                            +"Zoom in"
                        }
                        button {
                            id = "zoom-reset"
                            +"Reset"
                        }
                    }
                }
            }
            storyElement(storyId = storyId) {
                id = "story-element"
            }

        }
    }
}
