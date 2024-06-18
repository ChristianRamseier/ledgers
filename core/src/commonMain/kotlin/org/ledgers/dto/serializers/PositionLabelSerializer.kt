package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.scenario.PositionLabel

object PositionLabelSerializer : KSerializer<PositionLabel> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PositionLabel", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PositionLabel {
        return PositionLabel(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: PositionLabel) {
        return encoder.encodeString(value.label)
    }

}
