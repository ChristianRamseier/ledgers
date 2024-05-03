package org.ledgers.dto

import kotlinx.serialization.Serializable



@Serializable
data class BoxDto(val x: Int, val y: Int, val width: Int, val height: Int) {

}
