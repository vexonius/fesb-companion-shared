package dev.etino.fcshared

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant


object TimetableDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StartDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        val raw = decoder.decodeString()
        val epochMillis = raw
            .removePrefix("/Date(")
            .removeSuffix(")/")
            .toLong()
        return Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
    }
}

object ColorSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Long {
        val colorStr = decoder.decodeString()
        return when (colorStr) {
            "Blue" -> 0xff0060ff
            "Yellow" -> 0xffe5c700
            "Orange" -> 0xffff6600
            "Purple" -> 0xffa200ff
            "Red" -> 0xffff0000
            "Green" -> 0xff0b9700
            else -> 0x00ffffff
        }
    }

    override fun serialize(encoder: Encoder, value: Long) {
    }
}