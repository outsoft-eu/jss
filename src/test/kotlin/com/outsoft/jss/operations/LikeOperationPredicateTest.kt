package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.extensions.LikePredicateExtension.ESCAPE_CHARACTER
import com.outsoft.jss.extensions.LikePredicateExtension.LIKE_SYMBOL
import com.outsoft.jss.extensions.LikePredicateExtension.maskLikeValue
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
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

class LikeOperationPredicateTest {
    private val criteria = SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.LIKE)
    private lateinit var builderMock: CriteriaBuilder
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>
    private lateinit var expectedPredicate: Predicate
    private val operationPredicate = LikeOperationPredicate()

    @BeforeEach
    fun setUp() {
        builderMock = mock()
        rootMock = mock<Root<Any>>(defaultAnswer = Mockito.RETURNS_MOCKS)
        pathMock = mock()
        expectedPredicate = mock()
    }

    @Test
    fun `should build like predicate for string`() {
        whenever(rootMock.buildPath<String, Any>(criteria.key)).thenReturn(pathMock)
        val lower = mock<Expression<String>>()
        whenever(builderMock.lower(anyOrNull())).thenReturn(lower)
        whenever(
            builderMock.like(
                lower,
                "$LIKE_SYMBOL${maskLikeValue(criteria.value?.lowercase())}$LIKE_SYMBOL",
                ESCAPE_CHARACTER
            )
        ).thenReturn(expectedPredicate)
        assertEquals(expectedPredicate, operationPredicate.buildPredicate(builderMock, rootMock, criteria))
    }
}