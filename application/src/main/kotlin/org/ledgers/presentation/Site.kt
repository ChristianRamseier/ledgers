package org.ledgers.presentation

import kotlinx.html.*
import kotlinx.html.consumers.delayed
import kotlinx.html.stream.HTMLStreamBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Site {
    companion object {

        val TIMESTAMP = Regex("[^0-9]").replace(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()), "")

        fun getHtmlFor(browserTitle: String, pageDescription: String = "", content: SECTION.() -> Unit = {}): String {
            val builder = StringBuilder()
            builder.append("<!DOCTYPE html>\r\n")
            HTMLStreamBuilder(builder, true, true).delayed().html(lang = "en") {
                attributes["lang"] = "en"
                head {
                    title { +browserTitle }
                    link(href = "/canvas.css?$TIMESTAMP", rel = "stylesheet")
                    link(href = "/ledgers.css?$TIMESTAMP", rel = "stylesheet")
                    link(href = "/favicon.svg", rel = "icon")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                    meta(name = "description", content = "$pageDescription")
                }
                body {
                    section {
                        content()
                    }
                    script(src = "/prism.js?$TIMESTAMP") {}
                    script(src = "/canvas.js?$TIMESTAMP") {}
                    script(src = "/kotlin-kotlin-stdlib.js?$TIMESTAMP") {}
                    script(src = "/kotlin_org_jetbrains_kotlin_kotlin_dom_api_compat.js?$TIMESTAMP") {}
                    script(src = "/uuid.js?$TIMESTAMP") {}
                    script(src = "/ledgers-core.js?$TIMESTAMP") {}
                    script(src = "/ledgers.js?$TIMESTAMP") {}
                }
            }
            return builder.toString()
        }
    }
}

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.html(lang: String, crossinline block: HTML.() -> Unit = {}): T = HTML(attributesMapOf("lang", lang), this, null).visitAndFinalize(this, block)
