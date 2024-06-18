package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.scenario.AccountReference

object AccountReferenceSerializer : KSerializer<AccountReference> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("AccountReference", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AccountReference {
        return AccountReference(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: AccountReference) {
        return encoder.encodeString(value.reference)
    }

}
