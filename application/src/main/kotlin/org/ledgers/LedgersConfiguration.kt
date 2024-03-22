package org.ledgers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.ledgers.domain.StoryId
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


@Configuration
class LedgersConfiguration {

    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return OBJECT_MAPPER_CUSTOMIZER
    }

    @Bean
    fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.poolSize = 10
        return threadPoolTaskScheduler
    }

    companion object {

        private val OBJECT_MAPPER_CUSTOMIZER =
            Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
                registerString(builder, StoryId::class.java, { id -> id.toString() }, { id -> StoryId(id) })
                registerInt(builder, Version::class.java, { version -> version.version }, { version -> Version(version) })
                registerLong(builder, LedgerId::class.java, { id -> id.id }, { id -> LedgerId(id) })
                registerLong(builder, OrganizationId::class.java, { id -> id.id }, { id -> OrganizationId(id) })
            }

        fun objectMapper(): ObjectMapper {
            val objectMapper = ObjectMapper()
            val jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
            OBJECT_MAPPER_CUSTOMIZER.customize(jackson2ObjectMapperBuilder)
            jackson2ObjectMapperBuilder.configure(objectMapper)
            return objectMapper
        }

        private fun <T> registerString(
            builder: Jackson2ObjectMapperBuilder,
            java: Class<T>,
            serializer: (i: T) -> String,
            deserializer: (i: String) -> T
        ) {
            val stdDeserializer = object : StdDeserializer<T>(java) {
                override fun deserialize(parser: JsonParser, context: DeserializationContext): T? {
                    val valueAsString = parser.valueAsString
                    if (valueAsString != null) {
                        return deserializer(valueAsString)
                    }
                    return null
                }
            }
            val stdSerializer = object : StdSerializer<T>(java) {
                override fun serialize(value: T, generator: JsonGenerator, context: SerializerProvider) {
                    if (value == null) {
                        generator.writeNull()
                    } else {
                        generator.writeString(serializer(value))
                    }
                }
            }
            builder.serializerByType(java, stdSerializer)
            builder.deserializerByType(java, stdDeserializer)
        }

        private fun <T> registerInt(
            builder: Jackson2ObjectMapperBuilder,
            java: Class<T>,
            serializer: (i: T) -> Int,
            deserializer: (i: Int) -> T
        ) {
            val stdDeserializer = object : StdDeserializer<T>(java) {
                override fun deserialize(parser: JsonParser, context: DeserializationContext): T? {
                    if (parser.currentToken.isNumeric) {
                        return deserializer(parser.valueAsInt)
                    }
                    return null
                }
            }
            val stdSerializer = object : StdSerializer<T>(java) {
                override fun serialize(value: T, generator: JsonGenerator, context: SerializerProvider) {
                    if (value == null) {
                        generator.writeNull()
                    } else {
                        generator.writeNumber(serializer(value))
                    }
                }
            }
            builder.serializerByType(java, stdSerializer)
            builder.deserializerByType(java, stdDeserializer)
        }

        private fun <T> registerLong(
            builder: Jackson2ObjectMapperBuilder,
            java: Class<T>,
            serializer: (i: T) -> Long,
            deserializer: (i: Long) -> T
        ) {
            val stdDeserializer = object : StdDeserializer<T>(java) {
                override fun deserialize(parser: JsonParser, context: DeserializationContext): T? {
                    if (parser.currentToken.isNumeric) {
                        return deserializer(parser.valueAsLong)
                    }
                    return null
                }
            }
            val stdSerializer = object : StdSerializer<T>(java) {
                override fun serialize(value: T, generator: JsonGenerator, context: SerializerProvider) {
                    if (value == null) {
                        generator.writeNull()
                    } else {
                        generator.writeNumber(serializer(value))
                    }
                }
            }
            builder.serializerByType(java, stdSerializer)
            builder.deserializerByType(java, stdDeserializer)
        }

    }

}
