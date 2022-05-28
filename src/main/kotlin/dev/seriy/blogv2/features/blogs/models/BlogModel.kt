package dev.seriy.blogv2.features.blogs.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
data class BlogModel(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val tittle: String,
    val login: String,
    val userName: String,
    val photosurl: List<String>?,
    val timelong: Long,
    val description: String
)

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}

fun stringToArray(str: String): List<String> {
    return str.split(",")
}

fun arrayToString(arr: List<String>?): String {
    return arr?.joinToString(",") ?: ""
}
