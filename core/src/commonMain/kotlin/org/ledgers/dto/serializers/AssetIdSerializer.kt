package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.architecture.AssetId

object AssetIdSerializer : KSerializer<AssetId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("AssetId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AssetId {
        return AssetId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: AssetId) {
        return encoder.encodeString(value.id)
    }

}
