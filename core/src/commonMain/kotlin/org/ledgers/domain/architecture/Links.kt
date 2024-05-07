package org.ledgers.domain.architecture

import org.ledgers.domain.component.ComponentReference
import kotlin.js.JsExport

@JsExport
data class Links(val links: List<Link> = emptyList()) {

    val numberOfAssets get() = links.size
    val last get() = links.last()

    fun add(link: Link): Links {
        if (links.contains(link)) {
            throw RuntimeException("Link already exists")
        }
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

}
