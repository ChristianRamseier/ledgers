package org.ledgers.presentation

import kotlinx.html.*
import org.ledgers.domain.Story
import org.ledgers.presentation.components.editableElement

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
                attributes["storyId"] = storyId
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
                    div {
                        id = "components-controls"
                        button {
                            id = "add-organization"
                            +"Add Organization"
                        }
                        button {
                            id = "add-ledger"
                            +"Add Ledger"
                        }
                        button {
                            id = "add-asset"
                            +"Add Asset"
                        }
                    }
                    div {
                        id = "components-list"
                    }
                }
                div {
                    id = "chapters"
                    editableElement {
                        id = "chapter-name"
                    }
                    div {
                        id = "chapter-changes"
                    }
                    editableElement {
                        id = "chapter-scenario-step-name"
                    }
                    div {
                        id = "chapter-scenario-step-line"
                        div {
                            id = "chapter-scenario-steps"
                        }
                        button {
                            id = "add-chapter-scenario-step"
                            +"Add Step"
                        }
                    }
                    div {
                        id = "chapters-line"
                        div {
                            id = "chapters-list"
                        }
                        button {
                            id = "add-chapter"
                            +"Add Chapter"
                        }
                    }
                }
                div {
                    id = "editors"
                    div {
                        id = "ledger-editor"
                        input(type = InputType.text) {
                            id = "ledger-name"
                        }
                        select {
                            id = "ledger-organization"
                        }
                        button {
                            id = "ledger-apply"
                            +"Apply"
                        }
                    }
                    div {
                        id = "organization-editor"
                        input(type = InputType.text) {
                            id = "organization-name"
                        }
                        button {
                            id = "organization-apply"
                            +"Apply"
                        }
                    }
                    div {
                        id = "asset-editor"
                        input(type = InputType.text) {
                            id = "asset-name"
                        }
                        label {
                            radioInput(name = "asset-type") {
                                id = "asset-type-cash"
                                value = "Cash"
                            }
                            +"Cash"
                        }
                        label {
                            radioInput(name = "asset-type") {
                                id = "asset-type-security"
                                value = "Security"

                            }
                            +"Security"
                        }
                        button {
                            id = "asset-apply"
                            +"Apply"
                        }
                    }

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
        }
    }
}
