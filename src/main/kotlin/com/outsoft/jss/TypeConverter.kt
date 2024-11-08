package com.outsoft.jss

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

private const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DATE_PATTERN = "yyyy-MM-dd"

open class TypeConverter {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

    open fun <T> convert(value: String, type: Class<T>): Any =
        when (type) {
            Long::class.javaObjectType -> value.toLong()
            Long::class.java -> value.toLong()
            Int::class.javaObjectType -> value.toInt()
            Int::class.java -> value.toInt()
            Boolean::class.javaObjectType -> value.toBooleanStrict()
            Boolean::class.java -> value.toBooleanStrict()
            LocalDateTime::class.java -> value.toLocalDateTime()
            LocalDate::class.java -> value.toLocalDate()
            BigDecimal::class.java -> value.toBigDecimal()
            ZoneOffset::class.java -> ZoneOffset.of(value)
            ZonedDateTime::class.java -> ZonedDateTime.parse(value)
            Double::class.java -> value.toDouble()
            Double::class.javaObjectType -> value.toDouble()
            UUID::class.java -> UUID.fromString(value)
            else -> if (type.isEnum) {
                value.parseEnum(type)
            } else {
                value
            }
        }

    private fun <T> String.parseEnum(type: Class<T>) =
        (type.enumConstants?.firstOrNull { (it as Enum<*>).name == this.uppercase() }
            ?: throw com.outsoft.jss.CriteriaBadRequestException("Enum not found"))

    private fun String.toLocalDateTime(): LocalDateTime =
        try {
            ZonedDateTime.parse(this, dateTimeFormatter).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
        } catch (ex: DateTimeParseException) {
            throw DateTimeParseException(
                "${ex.message}. Allowed date format: $DATE_TIME_PATTERN",
                ex.parsedString, ex.errorIndex, ex.cause
            )
        }

    private fun String.toLocalDate(): LocalDate =
        try {
            LocalDate.parse(this, dateFormatter)
        } catch (ex: DateTimeParseException) {
            throw DateTimeParseException(
                "${ex.message}. Allowed date format: $DATE_PATTERN", ex.parsedString, ex.errorIndex, ex.cause
            )
        }
}
