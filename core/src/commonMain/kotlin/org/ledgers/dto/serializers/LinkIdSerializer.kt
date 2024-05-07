package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.architecture.AssetId
import org.ledgers.domain.architecture.LinkId

object LinkIdSerializer : KSerializer<LinkId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LinkId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LinkId {
        return LinkId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LinkId) {
        return encoder.encodeString(value.id)
    }

}
