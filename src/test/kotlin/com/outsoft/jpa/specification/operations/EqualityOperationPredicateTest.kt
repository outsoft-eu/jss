package com.outsoft.jpa.specification.operations

import com.outsoft.jpa.specification.SearchCriteria
import com.outsoft.jpa.specification.TypeConverter
import com.outsoft.jpa.specification.extensions.PathBuilderExtension.buildPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class EqualityOperationPredicateTest {
    private val criteria = SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.EQUALITY)
    private val builderMock = mock<CriteriaBuilder>()
    private val rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
    private val pathMock = mock<Path<String>>()
    private val expectedPredicate = mock<Predicate>()

    private val typeConverter = mock<TypeConverter>()
    private val predicate = EqualityOperationPredicate(typeConverter)

    @Test
    fun `should build equality predicate`() {
        val convertedValue = Long.MAX_VALUE
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        whenever(pathMock.javaType).thenReturn(String::class.java)
        whenever(typeConverter.convert(criteria.value!!, String::class.java)).thenReturn(convertedValue)
        whenever(builderMock.equal(pathMock, convertedValue)).thenReturn(expectedPredicate)
        assertEquals(expectedPredicate, predicate.buildPredicate(builderMock, rootMock, criteria))
    }

}