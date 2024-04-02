package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Ledgers(
    val ledgers: List<Ledger> = emptyList()
) {

    fun getByReference(reference: ComponentReference): Ledger {
        val ledger = ledgers.find { it.reference == reference }
        return ledger ?: throw RuntimeException("No ledger with reference $reference")
    }

    fun toArray(): Array<Ledger> {
        return ledgers.toTypedArray()
    }

    fun addOrReplace(ledger: Ledger): Ledgers {
        return Ledgers(ledgers.filter { it.reference != ledger.reference }.plus(ledger))
    }

    fun remove(reference: ComponentReference): Ledgers {
        return Ledgers(ledgers.filter { it.reference != reference })
    }


}
