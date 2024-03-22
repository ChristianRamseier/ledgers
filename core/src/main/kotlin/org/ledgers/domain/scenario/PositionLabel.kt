package org.ledgers.domain.scenario

data class PositionLabel(val label: String) {
    companion object {
        val NONE = PositionLabel("")
    }
}
