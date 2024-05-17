package org.ledgers.domain.stage

import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType
import kotlin.js.JsExport

@JsExport
data class AnchorReference(
    val ledgerReference: ComponentReference,
    val anchor: Anchor
) {
    init {
        if (ledgerReference.type != ComponentType.Ledger) {
            throw RuntimeException("Links can only connect ledgers, but got: ${ledgerReference.type}")
        }
    }

    val ledgerId: LedgerId get() = ledgerReference.id as LedgerId

    override fun toString(): String {
        return "$ledgerReference.anchor.${anchor.toString().lowercase()}"
    }

    companion object {
        fun fromString(string: String): AnchorReference {
            val parts = string.split(".")
            if (parts[1] != "anchor" && parts.size != 3) {
                throw RuntimeException("Invalid format of anchor reference: $string")
            }
            val ledgerReference = ComponentReference.fromString(parts[0])
            val anchor = Anchor.fromString(parts[2])
            return AnchorReference(ledgerReference, anchor)
        }
    }
}
