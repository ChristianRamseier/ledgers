package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.scenario.Quantity

object QuantitySerializer : KSerializer<Quantity> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Quantity", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Quantity {
        return Quantity(decoder.decodeDouble())
    }

    override fun serialize(encoder: Encoder, value: Quantity) {
        return encoder.encodeDouble(value.quantity)
    }

}
