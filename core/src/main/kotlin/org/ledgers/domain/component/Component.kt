package org.ledgers.domain.component

import org.ledgers.domain.Version

interface Component {
    val id: ComponentId
    val version: Version
    val type: ComponentType
    val reference get() = ComponentReference(type, id, version)
}
