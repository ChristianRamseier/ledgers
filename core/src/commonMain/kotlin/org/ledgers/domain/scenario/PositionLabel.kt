package org.ledgers.domain.scenario

data class PositionLabel(val label: String) {

    override fun toString(): String {
        return label
    }

    companion object {
        val None = PositionLabel("")
    }
}
