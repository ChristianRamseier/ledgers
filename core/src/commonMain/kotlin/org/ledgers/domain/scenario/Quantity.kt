package org.ledgers.domain.scenario

import kotlin.math.round

data class Quantity(
    val quantity: Double
) {

    /**
     * Returns the quantity as string, rounded to two decimal points.
     */
    override fun toString(): String {
        val rounded = round(quantity * 100.0) / 100.0
        return rounded.toString()
    }

    operator fun plus(delta: Quantity): Quantity {
        return Quantity(quantity + delta.quantity)
    }

    operator fun minus(delta: Quantity): Quantity {
        return Quantity(quantity - delta.quantity)
    }

}
