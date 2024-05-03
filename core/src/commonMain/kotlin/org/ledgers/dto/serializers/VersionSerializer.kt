package org.ledgers.dto.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.AssetId

object VersionSerializer : KSerializer<Version> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Version", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Version {
        return Version(decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: Version) {
        return encoder.encodeInt(value.version)
    }

}
