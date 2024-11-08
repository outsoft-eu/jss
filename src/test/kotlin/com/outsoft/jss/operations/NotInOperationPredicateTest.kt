package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
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

class NotInOperationPredicateTest {

    private val listValues = listOf("value1", "value2")
    private val criteria =
        SearchCriteria(key = "key", value = listValues.joinToString(), operation = SearchCriteria.Operation.NOT_IN)
    private lateinit var builderMock: CriteriaBuilder
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>
    private lateinit var expectedPredicate: Predicate

    private val typeConverter = mock<TypeConverter>()
    private val predicate = NotInOperationPredicate(typeConverter)

    @BeforeEach
    fun setUp() {
        builderMock = mock<CriteriaBuilder>()
        rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
        pathMock = mock<Path<String>>()
        expectedPredicate = mock<Predicate>()
    }

    @Test
    fun `should create conjunction predicate for empty list`() {
        whenever(builderMock.conjunction()).thenReturn(expectedPredicate)
        assertEquals(
            expectedPredicate,
            predicate.buildPredicate(builderMock, rootMock, criteria.copy(value = ""))
        )
    }

    @Test
    fun `should create in predicate for list of values`() {
        listValues.forEach {
            whenever(typeConverter.convert(it, String::class.java)).thenReturn(it)
        }
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        whenever(pathMock.javaType).thenReturn(String::class.java)

        val inPredicate = mock<Predicate>()
        whenever(pathMock.`in`(listValues)).thenReturn(inPredicate)

        val notPredicate = mock<Predicate>()
        whenever(builderMock.not(inPredicate)).thenReturn(notPredicate)

        val isNull = mock<Predicate>()
        whenever(builderMock.isNull(pathMock)).thenReturn(isNull)

        whenever(builderMock.or(notPredicate, isNull)).thenReturn(expectedPredicate)

        assertEquals(
            expectedPredicate,
            predicate.buildPredicate(builderMock, rootMock, criteria)
        )
    }
}