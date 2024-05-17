package org.ledgers.domain.stage

import kotlin.js.JsExport

@JsExport
enum class Anchor {
    Top, Left, Right, Bottom;

    companion object {
        fun fromString(string: String): Anchor {
            return values().find { it.name.equals(string, ignoreCase = true) } ?: throw RuntimeException("Unknown anchor: $string")
        }
    }
}
