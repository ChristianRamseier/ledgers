package org.ledgers.domain.stage

import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class LedgerOnStage(
    override val reference: ComponentReference,
    val box: Box
) : ComponentOnStage {

    init {
        if(reference.type != ComponentType.Ledger) {
            throw RuntimeException("Ledger on stage must have a ledgers component reference, but got: $reference")
        }
    }

}
