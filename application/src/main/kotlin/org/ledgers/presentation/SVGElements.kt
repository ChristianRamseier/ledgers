package org.ledgers.presentation

import kotlinx.html.*


class Graphic(consumer: TagConsumer<*>) :
    HTMLTag("g", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false),
    HtmlInlineTag

fun SVG.g(block: Graphic.() -> Unit = {}) {
    Graphic(consumer).visit(block)
}



class Definitions(consumer: TagConsumer<*>) :
    HTMLTag("defs", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false),
    HtmlInlineTag

fun SVG.defs(block: Definitions.() -> Unit = {}) {
    Definitions(consumer).visit(block)
}


class Marker(consumer: TagConsumer<*>) :
    HTMLTag("markers", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false),
    HtmlInlineTag

fun Definitions.marker(block: Marker.() -> Unit = {}) {
    Marker(consumer).visit(block)
}

class Polygon(consumer: TagConsumer<*>) :
    HTMLTag("polygon", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false),
    HtmlInlineTag

fun Marker.polygon(block: Polygon.() -> Unit = {}) {
    Polygon(consumer).visit(block)
}
