package org.ledgers.presentation.components

import kotlinx.html.*

@Suppress("unused")
open class EditableElement(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("editable-element", consumer, initialAttributes, null, false, false), CommonAttributeGroupFacadeFlowInteractivePhrasingContent


@HtmlTagMarker
inline fun FlowOrHeadingContent.editableElement(classes : String? = null, crossinline block : EditableElement.() -> Unit = {}) : Unit = EditableElement(attributesMapOf("class", classes), consumer).visit(block)
