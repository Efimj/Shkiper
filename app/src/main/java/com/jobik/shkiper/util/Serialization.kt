package com.jobik.shkiper.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class NullableLocalDateSerializer : KSerializer<LocalDate?> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate?) {
        if (value == null) {
            encoder.encodeString(null.toString())
        } else {
            encoder.encodeString(value.format(formatter))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        return try {
            LocalDate.parse(decoder.decodeString(), formatter)
        } catch (e: Exception) {
            return null
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class NullableLocalDateTimeSerializer : KSerializer<LocalDateTime?> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value == null) {
            encoder.encodeString(null.toString())
        } else {
            encoder.encodeString(value.format(formatter))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        return try {
            LocalDateTime.parse(decoder.decodeString(), formatter)
        } catch (e: Exception) {
            return null
        }
    }
}