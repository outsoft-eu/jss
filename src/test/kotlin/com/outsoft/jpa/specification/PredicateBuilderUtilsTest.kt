package com.outsoft.jpa.specification

import com.outsoft.jpa.specification.PredicateBuilderUtils.notEqualsWithNullsPredicate
import com.outsoft.jpa.specification.PredicateBuilderUtils.parseListOfValues
import com.outsoft.jpa.specification.PredicateBuilderUtils.withNullValues
import com.outsoft.jpa.specification.PredicateBuilderUtils.withoutNullValues
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

private const val STRING_WITH_3_VALUES_WITH_COMMA = "val1, val 2, val"
private const val STRING_WITH_3_VALUES_WITH_SEMICOLON = "val1;val 2;val"
private const val VALUE = "test"

class PredicateBuilderUtilsTest {
    private lateinit var builderMock: CriteriaBuilder
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>
    private lateinit var expectedPredicate: Predicate

    @BeforeEach
    fun setUp() {
        builderMock = mockk()
        rootMock = mockk(relaxed = true)
        pathMock = mockk()
        expectedPredicate = mockk()
    }

    @Test
    fun `should build not equals predicate with null values`() {
        every {
            builderMock.or(
                builderMock.notEqual(pathMock, VALUE),
                builderMock.isNull(pathMock)
            )
        }.returns(expectedPredicate)

        assertEquals(expectedPredicate, builderMock.notEqualsWithNullsPredicate(pathMock, VALUE))
    }

    @Test
    fun `should add null values condition to predicate`() {
        every {
            builderMock.or(
                expectedPredicate,
                builderMock.isNull(pathMock)
            )
        }.returns(expectedPredicate)
        assertEquals(expectedPredicate, builderMock.withNullValues(expectedPredicate, pathMock))
    }

    @Test
    fun `should add not null values condition to predicate`() {
        every {
            builderMock.or(
                expectedPredicate,
                builderMock.isNotNull(pathMock)
            )
        }.returns(expectedPredicate)
        assertEquals(expectedPredicate, builderMock.withoutNullValues(expectedPredicate, pathMock))
    }

    @Test
    fun `should parse string to list of values separated by comma`() {
        val actualList = parseListOfValues(STRING_WITH_3_VALUES_WITH_COMMA)
        val expectedList = STRING_WITH_3_VALUES_WITH_COMMA.split(", ")
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `should parse string to list of values separated by semicolon`() {
        val actualList = parseListOfValues(STRING_WITH_3_VALUES_WITH_SEMICOLON)
        val expectedList = STRING_WITH_3_VALUES_WITH_SEMICOLON.split(";")
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `should parse empty string to empty list`() {
        assertTrue(parseListOfValues("").isEmpty())
    }
}