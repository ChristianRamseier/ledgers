package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.scenario.action.ActionId

object ActionIdSerializer : KSerializer<ActionId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ActionId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ActionId {
        return ActionId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ActionId) {
        return encoder.encodeString(value.id)
    }

}
