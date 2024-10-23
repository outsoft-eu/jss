package com.outsoft.jpa.specification.operations

import com.outsoft.jpa.specification.SearchCriteria
import com.outsoft.jpa.specification.extensions.LikePredicateExtension.ESCAPE_CHARACTER
import com.outsoft.jpa.specification.extensions.LikePredicateExtension.LIKE_SYMBOL
import com.outsoft.jpa.specification.extensions.LikePredicateExtension.maskLikeValue
import com.outsoft.jpa.specification.extensions.PathBuilderExtension.buildPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class NotLikeOperationPredicateTest {
    private val criteria = SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.LIKE)
    private lateinit var builderMock: CriteriaBuilder
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>
    private lateinit var expectedPredicate: Predicate
    private val operationPredicate = NotLikeOperationPredicate()

    @BeforeEach
    fun setUp() {
        builderMock = mock()
        rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
        pathMock = mock()
        expectedPredicate = mock()
    }

    @Test
    fun `should build not like predicate for string`() {
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        val lower = mock<Expression<String>>()
        whenever(builderMock.lower(anyOrNull())).thenReturn(lower)
        val notLike = mock<Predicate>()
        whenever(
            builderMock.notLike(
                lower,
                "$LIKE_SYMBOL${maskLikeValue(criteria.value?.lowercase())}$LIKE_SYMBOL",
                ESCAPE_CHARACTER
            )
        ).thenReturn(notLike)
        val isNull = mock<Predicate>()
        whenever(builderMock.isNull(pathMock)).thenReturn(isNull)
        whenever(builderMock.or(notLike, isNull)).thenReturn(expectedPredicate)
        assertEquals(expectedPredicate, operationPredicate.buildPredicate(builderMock, rootMock, criteria))
    }
}