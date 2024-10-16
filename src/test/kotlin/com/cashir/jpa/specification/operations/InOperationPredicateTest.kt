package com.cashir.jpa.specification.operations

import com.cashir.jpa.specification.SearchCriteria
import com.cashir.jpa.specification.TypeConverter
import com.cashir.jpa.specification.extensions.PathBuilderExtension.buildPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class InOperationPredicateTest {
    private val listValues = listOf("value1", "value2")
    private val criteria =
        SearchCriteria(key = "key", value = listValues.joinToString(), operation = SearchCriteria.Operation.IN)
    private lateinit var builderMock: CriteriaBuilder
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>
    private lateinit var expectedPredicate: Predicate

    private val typeConverter = mock<TypeConverter>()
    private val predicate = InOperationPredicate(typeConverter)

    @BeforeEach
    fun setUp() {
        builderMock = mock<CriteriaBuilder>()
        pathMock = mock<Path<String>>()
        rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
        expectedPredicate = mock<Predicate>()
    }

    @Test
    fun `should create disjunction predicate for empty list`() {
        whenever(builderMock.disjunction()).thenReturn(expectedPredicate)
        assertEquals(
            expectedPredicate,
            predicate.buildPredicate(builderMock, rootMock, criteria.copy(value = ""))
        )
    }

    @Test
    fun `should create in predicate for list of values`() {
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        whenever(pathMock.javaType).thenReturn(String::class.java)
        whenever(pathMock.`in`(listValues)).thenReturn(expectedPredicate)
        listValues.forEach {
            whenever(typeConverter.convert(it, String::class.java)).thenReturn(it)
        }
        assertEquals(
            expectedPredicate,
            predicate.buildPredicate(builderMock, rootMock, criteria)
        )
    }
}