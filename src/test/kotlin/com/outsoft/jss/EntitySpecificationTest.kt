package com.outsoft.jss

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class EntitySpecificationTest {

    @Test
    fun `should create predicate from PredicateBuilder`() {
        val root: Root<Any> = mock()
        val builder: CriteriaBuilder = mock()
        val criteria: SearchCriteria = mock()

        val expectedResult: Predicate = mock()

        val predicateBuilder: com.outsoft.jss.PredicateBuilder = mock()
        whenever(predicateBuilder.buildPredicate(builder, root, criteria)).thenReturn(expectedResult)

        assertEquals(
            expectedResult,
            com.outsoft.jss.EntitySpecification<Any>(criteria, predicateBuilder).toPredicate(root, mockk(), builder)
        )
    }
}