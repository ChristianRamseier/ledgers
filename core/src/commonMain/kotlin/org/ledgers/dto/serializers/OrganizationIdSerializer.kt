package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.architecture.OrganizationId

object OrganizationIdSerializer : KSerializer<OrganizationId> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("OrganizationId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OrganizationId {
        return OrganizationId(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: OrganizationId) {
        return encoder.encodeString(value.id)
    }

}
