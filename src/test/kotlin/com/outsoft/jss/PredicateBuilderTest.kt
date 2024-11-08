package com.outsoft.jss

import com.outsoft.jss.operations.EqualityOperationPredicate
import com.outsoft.jss.operations.GreaterThanOperationPredicate
import com.outsoft.jss.operations.InOperationPredicate
import com.outsoft.jss.operations.LessThanOperationPredicate
import com.outsoft.jss.operations.LikeOperationPredicate
import com.outsoft.jss.operations.NegationOperationPredicate
import com.outsoft.jss.operations.NotInOperationPredicate
import com.outsoft.jss.operations.NotLikeOperationPredicate
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class PredicateBuilderTest {
    private lateinit var predicateBuilder: com.outsoft.jss.PredicateBuilder
    private val typeConverter: TypeConverter = mock()
    private val equalityCriteria =
        SearchCriteria(key = "key", value = "value", operation = SearchCriteria.Operation.EQUALITY)
    private val builderMock = mockk<CriteriaBuilder>()
    private val rootMock = mockk<Root<Any>>(relaxed = true)

    @AfterEach
    fun cleanUp() = unmockkAll()

    @Test
    fun `should execute equality operation`() {
        val expectedResult = mockOperation<EqualityOperationPredicate>(criteria = equalityCriteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                equalityCriteria
            )
        )
    }

    @Test
    fun `should execute negation operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.NEGATION)
        val expectedResult = mockOperation<NegationOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute like operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.LIKE)
        val expectedResult = mockOperation<LikeOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute not like operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.NOT_LIKE)
        val expectedResult = mockOperation<NotLikeOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute greater than operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.GREATER_THAN)
        val expectedResult = mockOperation<GreaterThanOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute less than operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.LESS_THAN)
        val expectedResult = mockOperation<LessThanOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute in operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.IN)
        val expectedResult = mockOperation<InOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should execute not in operation`() {
        val criteria = equalityCriteria.copy(operation = SearchCriteria.Operation.NOT_IN)
        val expectedResult = mockOperation<NotInOperationPredicate>(criteria)
        assertEquals(
            expectedResult, predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                criteria
            )
        )
    }

    @Test
    fun `should throw CriteriaBadRequestException for unexpected scenario`() {
        mockkConstructor(EqualityOperationPredicate::class)
        every {
            constructedWith<EqualityOperationPredicate>(EqMatcher(typeConverter)).buildPredicate(
                builderMock,
                rootMock,
                equalityCriteria
            )
        } throws RuntimeException()
        predicateBuilder = com.outsoft.jss.PredicateBuilder(typeConverter)
        assertThrows(com.outsoft.jss.CriteriaBadRequestException::class.java) {
            predicateBuilder.buildPredicate(
                builderMock,
                rootMock,
                equalityCriteria
            )
        }
    }

    private inline fun <reified T : com.outsoft.jss.OperationPredicate> mockOperation(criteria: SearchCriteria): Predicate =
        mockk<Predicate>().also {
            mockkConstructor(T::class)
            every {
                anyConstructed<T>().buildPredicate(builderMock, rootMock, criteria)
            } returns it
            predicateBuilder = com.outsoft.jss.PredicateBuilder(typeConverter)
        }
}