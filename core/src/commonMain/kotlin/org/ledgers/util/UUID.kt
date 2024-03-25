package org.ledgers.util

import com.benasher44.uuid.uuid4

class UUID {
    companion object {
        fun randomAsString(): String {
            return uuid4().toString()
        }
    }
}
