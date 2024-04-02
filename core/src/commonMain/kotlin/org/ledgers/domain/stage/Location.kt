package org.ledgers.domain.stage

import kotlin.js.JsExport

@JsExport
data class Location(val x: Int, val y: Int) {
    companion object {
        val ZERO = Location(0, 0)
    }
}
