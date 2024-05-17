package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class LinkOnStage(
    override val reference: ComponentReference,
    val fromAnchor: Anchor,
    val toAnchor: Anchor
) : ComponentOnStage {

    init {
        if(reference.type != ComponentType.Link) {
            throw RuntimeException("Link on stage must have a links component reference, but got: $reference")
        }
    }

}
