package org.ledgers.domain.architecture

import org.ledgers.domain.Version
import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Links(val links: List<Link> = emptyList()) {

    val numberOfAssets get() = links.size
    val last get() = links.last()

    fun find(from: LedgerId, to: LedgerId): Link? {
        return links.find { it.from == from && it.to == to }
    }

    fun addLinkIfNotExists(from: LedgerId, to: LedgerId): Links {
        if (find(from, to) != null) {
            return this
        }
        val link = Link(LinkId.random(), Version.Zero, from, to)
        return Links(links.plus(link))
    }

    fun remove(reference: ComponentReference): Links {
        if (links.none { it.reference == reference }) {
            throw RuntimeException("Link with $reference not found")
        }
        return Links(links.filter { it.reference == reference })
    }


    fun change(reference: ComponentReference, from: LedgerId, to: LedgerId): Links {
        return Links(
            links.map {
                if (reference == it.reference) {
                    Link(it.id, it.version, from, to)
                } else {
                    it
                }
            }
        )
    }

    fun toArray(): Array<Link> {
        return links.toTypedArray()
    }

    fun getByReference(reference: ComponentReference): Link {
        val asset = links.find { it.reference == reference }
        return asset ?: throw RuntimeException("No link with reference $reference")
    }

    fun findBetween(from: LedgerId, to: LedgerId): Link? {
        return links.find { it.from == from && it.to == to }
    }


}
