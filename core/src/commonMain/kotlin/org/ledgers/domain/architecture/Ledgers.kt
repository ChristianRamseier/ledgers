package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Ledgers(
    val ledgers: List<Ledger> = emptyList()
) {

    val last get() = ledgers.last()

    fun getByReference(reference: ComponentReference): Ledger {
        val ledger = ledgers.find { it.reference == reference }
        return ledger ?: throw RuntimeException("No ledger with reference $reference")
    }

    fun getFirstById(id: LedgerId): Ledger {
        return ledgers
            .sortedBy { it.version }
            .find { it.id == id }
            ?: throw RuntimeException("No ledger with id $id")
    }

    fun contains(reference: ComponentReference): Boolean {
        return ledgers.any { it.reference == reference }
    }

    fun toArray(): Array<Ledger> {
        return ledgers.toTypedArray()
    }

    fun addOrReplace(ledger: Ledger): Ledgers {
        return Ledgers(ledgers.filter { it.reference != ledger.reference }.plus(ledger))
    }

    fun remove(reference: ComponentReference): Ledgers {
        return Ledgers(ledgers.filterNot { it.reference == reference })
    }

    fun withChangedLedger(reference: ComponentReference, name: String, ownerId: OrganizationId): Ledgers {
        return Ledgers(
            ledgers.map {
                if (it.reference == reference) {
                    it.copy(name = name, ownerId = ownerId)
                } else {
                    it
                }
            }
        )
    }


}
