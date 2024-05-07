package org.ledgers.domain.architecture

enum class Control {
    None, // View Only
    Shared, // Instruct
    Exclusive // Instruct and release other instructions
}
