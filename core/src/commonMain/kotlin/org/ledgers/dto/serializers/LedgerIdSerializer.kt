package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.architecture.LedgerId

object LedgerIdSerializer : KSerializer<LedgerId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LedgerId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LedgerId {
        return LedgerId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LedgerId) {
        return encoder.encodeString(value.id)
    }

}
