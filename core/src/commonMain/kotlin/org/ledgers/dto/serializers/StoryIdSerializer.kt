package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.StoryId

object StoryIdSerializer : KSerializer<StoryId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("StoryId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): StoryId {
        return StoryId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: StoryId) {
        return encoder.encodeString(value.id)
    }

}
