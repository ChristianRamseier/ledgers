package org.ledgers.domain.stage

import kotlin.js.JsExport

@JsExport
data class Box(val x: Int, val y: Int, val width: Int, val height: Int) {
    companion object {
        val ZERO = Box(0, 0, 100, 100)
    }
}
