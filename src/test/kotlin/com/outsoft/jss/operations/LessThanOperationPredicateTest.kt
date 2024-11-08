package com.outsoft.jss.operations

import com.outsoft.jss.CriteriaBadRequestException
import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class LessThanOperationPredicateTest {
    private val criteria = SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.LESS_THAN)
    private lateinit var typeConverter: TypeConverter
    private lateinit var operationPredicate: com.outsoft.jss.OperationPredicate

    @BeforeEach
    fun setUp() {
        typeConverter = mock()
        operationPredicate = LessThanOperationPredicate(typeConverter)
    }

    @Test
    fun `should build predicate for Long`() {
        assertPredicateBuildForValue(Long.MAX_VALUE)
    }

    @Test
    fun `should build predicate for String`() {
        assertPredicateBuildForValue(criteria.value!!)
    }

    @Test
    fun `should build predicate for Int`() {
        assertPredicateBuildForValue(Int.MAX_VALUE)
    }

    @Test
    fun `should build predicate for Boolean`() {
        assertPredicateBuildForValue(true)
    }

    @Test
    fun `should build predicate for LocalDateTime`() {
        assertPredicateBuildForValue(LocalDateTime.now())
    }

    @Test
    fun `should build predicate for LocalDate`() {
        assertPredicateBuildForValue(LocalDate.now())
    }

    @Test
    fun `should build predicate for BigDecimal`() {
        assertPredicateBuildForValue(BigDecimal.TEN)
    }

    @Test
    fun `should build predicate for Double`() {
        assertPredicateBuildForValue(Double.MAX_VALUE)
    }

    @Test
    fun `should throw exception for unknown type`() {
        assertThrows(com.outsoft.jss.CriteriaBadRequestException::class.java) {
            assertPredicateBuildForValue(
                CustomComparable()
            )
        }
    }

    @Test
    fun `should build predicate for ZonedDateTime`() {
        assertPredicateBuildForValue(ZonedDateTime.now())
    }

    private fun <T : Comparable<T>> assertPredicateBuildForValue(convertedValue: T) {
        whenever(typeConverter.convert(criteria.value!!, convertedValue.javaClass)).thenReturn(convertedValue)
        val expressionMock = mock<Expression<T>>()
        val pathMock = mock<Path<T>>()
            .also {
                whenever(it.javaType).thenReturn(convertedValue.javaClass)
                whenever(it.`as`(convertedValue::class.javaObjectType)).thenReturn(expressionMock)
            }

        val rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
            .also {
                whenever(it.buildPath<T, Any>(criteria.key)).thenReturn(pathMock)
            }

        val expectedPredicate = mock<Predicate>()
        val builderMock = mock<CriteriaBuilder>()
            .also {
                whenever(it.lessThanOrEqualTo(expressionMock, convertedValue)).thenReturn(expectedPredicate)
            }
        assertEquals(expectedPredicate, operationPredicate.buildPredicate(builderMock, rootMock, criteria))
    }

    private class CustomComparable : Comparable<CustomComparable> {
        override fun compareTo(other: CustomComparable): Int = 0
    }
}
