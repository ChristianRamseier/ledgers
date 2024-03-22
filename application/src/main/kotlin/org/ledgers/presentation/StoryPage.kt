package org.ledgers.presentation

import kotlinx.html.*
import org.ledgers.domain.Story

class StoryPage(private val id: String, private val story: Story?) : Page {

    override fun toHtml(): String {
        if (story == null) {
            return Site.getHtmlFor("Story not found") {
                +"No story found by id $id"
            }
        }
        return Site.getHtmlFor("${story.getDisplayName()} - Story") {

            div {
                id = "container"
                div {
                    id = "canvas-container"
                    svg {
                        id = "canvas-edges"
                        defs {
                            marker {
                                id = "arrowhead"
                                attributes["markerWidth"] = "10"
                                attributes["markerHeight"] = "8"
                                attributes["refX"] = "5"
                                attributes["refY"] = "4"
                                attributes["orient"] = "auto"
                                polygon { attributes["points"] = "0 0, 10 4, 0 8" }
                            }
                        }
                        g { id = "edge-paths" }

                    }

                    div {
                        id = "canvas-nodes"

                    }
                    div(classes = "theme-dark hidden") {
                        id = "output"
                        div(classes = "code-header") {
                            span(classes = "language") { +"Ledger Story" }
                            span(classes = "close-output") { +"×" }
                        }
                        div {
                            id = "output-code"
                            pre { code(classes = "language-json") { id = "positionsOutput" } }
                        }
                        div(classes = "code-footer") {
                            button(classes = "button-copy") { +"Copy code" }
                            button(classes = "button-download") { +"Download file" }
                        }
                    }
                }
                div {
                    id = "controls"
                    div {
                        id = "zoom-controls"
                        button {
                            id = "toggle-output"
                            +"Toggle output"
                        }
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
        }
    }
}
