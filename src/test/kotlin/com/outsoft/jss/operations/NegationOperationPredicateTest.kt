package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class NegationOperationPredicateTest {
    private val criteria = SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.NEGATION)
    private val builderMock = mock<CriteriaBuilder>()
    private val rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
    private val pathMock = mock<Path<String>>()
    private val expectedPredicate = mock<Predicate>()

    private val typeConverter = mock<TypeConverter>()
    private val predicate = NegationOperationPredicate(typeConverter)

    @Test
    fun `should build negation predicate`() {
        val convertedValue = Long.MAX_VALUE
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        whenever(pathMock.javaType).thenReturn(String::class.java)
        whenever(typeConverter.convert(criteria.value!!, String::class.java)).thenReturn(convertedValue)
        val notEqual = mock<Predicate>()
        val isNull = mock<Predicate>()
        whenever(builderMock.notEqual(pathMock, convertedValue)).thenReturn(notEqual)
        whenever(builderMock.isNull(pathMock)).thenReturn(isNull)
        whenever(builderMock.or(notEqual, isNull)).thenReturn(expectedPredicate)
        assertEquals(expectedPredicate, predicate.buildPredicate(builderMock, rootMock, criteria))
    }

    @Test
    fun `should build negation predicate for null value`() {
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        whenever(pathMock.javaType).thenReturn(String::class.java)
        val notEqual = mock<Predicate>()
        val isNotNull = mock<Predicate>()
        whenever(builderMock.notEqual(pathMock, null as String?)).thenReturn(notEqual)
        whenever(builderMock.isNotNull(pathMock)).thenReturn(isNotNull)
        whenever(builderMock.or(notEqual, isNotNull)).thenReturn(expectedPredicate)
        assertEquals(expectedPredicate, predicate.buildPredicate(builderMock, rootMock, criteria.copy(value = null)))
    }
}