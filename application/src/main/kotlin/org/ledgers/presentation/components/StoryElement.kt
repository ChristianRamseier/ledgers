package org.ledgers.presentation.components

import kotlinx.html.*

class StoryElement(initialAttributes: Map<String, String>, consumer: TagConsumer<*>) :
    HTMLTag(
        "story-element", consumer, initialAttributes,
        inlineTag = true,
        emptyTag = false
    ),
    HtmlInlineTag

fun FlowContent.storyElement(storyId: String, block: StoryElement.() -> Unit = {}) {
    StoryElement(attributesMapOf("story-id", storyId), consumer).visit(block)
}

