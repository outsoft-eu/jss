package com.outsoft.jss

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val LOCAL_DATE_TIME = "2022-11-16T09:20:39Z"
private const val INVALID_LOCAL_DATE_TIME = "66-66-16T09:66:66Y"
private const val DATE_PATTERN = "yyyy-MM-dd"
private const val LOCAL_DATE = "2022-11-16"
private const val INVALID_LOCAL_DATE = "44-44-44"
private const val ZONED_DATE_TIME = "2022-06-27T19:38:46+02:00"
private const val BIG_DECIMAL_STRING = "10"
private const val DOUBLE_STRING = "100.0"
private const val PLAIN_STRING = "string"

class TypeConverterTest {
    private val converter = TypeConverter()

    @Test
    fun `should return same string for unknown type`() {
        assertEquals(PLAIN_STRING, converter.convert(PLAIN_STRING, TypeConverter::class.java))
    }

    @Test
    fun `should return same string for String type`() {
        assertEquals(PLAIN_STRING, converter.convert(PLAIN_STRING, String::class.java))
    }

    @Test
    fun `should convert to primitive long`() {
        val expectedValue = Long.MAX_VALUE
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Long::class.java))
    }

    @Test
    fun `should convert to Long object`() {
        val expectedValue = Long.MAX_VALUE
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Long::class.javaObjectType))
    }

    @Test
    fun `should convert to Int object`() {
        val expectedValue = Int.MAX_VALUE
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Int::class.javaObjectType))
    }

    @Test
    fun `should convert to primitive int`() {
        val expectedValue = Int.MAX_VALUE
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Int::class.java))
    }

    @Test
    fun `should convert to primitive boolean`() {
        val expectedValue = true
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Boolean::class.java))
    }


    @Test
    fun `should convert to Boolean object`() {
        val expectedValue = true
        assertEquals(expectedValue, converter.convert(expectedValue.toString(), Boolean::class.javaObjectType))
    }

    @Test
    fun `should throw exception for incorrect Boolean`() {
        val value = "null"
        assertThrows<IllegalArgumentException> { converter.convert(value, Boolean::class.java) }
    }

    @Test
    fun `should convert to LocalDateTime`() {
        val formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
        val expectedValue =
            ZonedDateTime.parse(LOCAL_DATE_TIME, formatter).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
        assertEquals(expectedValue, converter.convert(LOCAL_DATE_TIME, LocalDateTime::class.java))
    }

    @Test
    fun `should throw exception to convert invalid LocalDateTime`() {
        assertThrows<DateTimeParseException> { converter.convert(INVALID_LOCAL_DATE_TIME, LocalDateTime::class.java) }
    }

    @Test
    fun `should convert to LocalDate`() {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val expectedValue = LocalDate.parse(LOCAL_DATE, formatter)
        assertEquals(expectedValue, converter.convert(LOCAL_DATE, LocalDate::class.java))
    }

    @Test
    fun `should convert to BigDecimal`() {
        val expectedValue = BigDecimal.TEN
        assertEquals(expectedValue, converter.convert(BIG_DECIMAL_STRING, BigDecimal::class.java))
    }

    @Test
    fun `should convert to primitive double`() {
        val expectedValue = DOUBLE_STRING.toDouble()
        assertEquals(expectedValue, converter.convert(DOUBLE_STRING, Double::class.java))
    }

    @Test
    fun `should convert to Double object`() {
        val expectedValue = DOUBLE_STRING.toDouble()
        assertEquals(expectedValue, converter.convert(DOUBLE_STRING, Double::class.javaObjectType))
    }

    @Test
    fun `should throw exception to convert invalid LocalDate`() {
        assertThrows<DateTimeParseException> { converter.convert(INVALID_LOCAL_DATE, LocalDate::class.java) }
    }

    @Test
    fun `should convert to ZonedDateTime`() {
        val expectedValue = ZonedDateTime.parse(ZONED_DATE_TIME)
        assertEquals(expectedValue, converter.convert(ZONED_DATE_TIME, ZonedDateTime::class.java))
    }
}
