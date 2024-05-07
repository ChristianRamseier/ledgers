package org.ledgers.domain.architecture

import kotlin.js.JsExport

@JsExport
data class SourceLabel(val label: String) {

    companion object {
        val None = SourceLabel("")
    }
}
