package org.ledgers.domain.architecture

class Ledgers {

    val ledgers: List<Ledger>

    constructor() {
        ledgers = emptyList()
    }

    constructor(ledgers: List<Ledger>) {
        this.ledgers = ledgers
    }

    fun add(ledger: Ledger): Ledgers {
        if (ledgers.any { it.id == ledger.id }) {
            throw RuntimeException("Ledger with name ${ledger.name} already exists")
        }
        return Ledgers(ledgers.plus(ledger))
    }

    fun remove(ledgerId: LedgerId): Ledgers {
        return Ledgers(ledgers.filter { it.id != ledgerId })
    }


}
