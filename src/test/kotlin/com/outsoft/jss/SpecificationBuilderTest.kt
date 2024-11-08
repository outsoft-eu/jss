package com.outsoft.jss

import com.outsoft.jss.SearchCriteria.Type
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.jpa.domain.Specification

class SpecificationBuilderTest {
    private val typeConverter: TypeConverter = mock()
    private val specificationBuilder = SpecificationBuilder(typeConverter)

    @AfterEach
    fun cleanUp() = unmockkAll()

    @Test
    fun shouldBuildAndCriteria() {
        val criteria = listOf(mockCriteria(Type.AND), mockCriteria(Type.AND))
        val spec = mockSpecificationChain()

        val result = specificationBuilder.build<Any>(criteria)
        criteria.forEach {
            verify {
                spec.and(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        assertEquals(spec, result)
    }

    @Test
    fun shouldBuildSecurityCriteria() {
        val criteria = listOf(mockCriteria(SearchCriteria.Type.SECURITY), mockCriteria(SearchCriteria.Type.SECURITY))
        val spec = mockSpecificationChain()

        val result = specificationBuilder.build<Any>(criteria)
        criteria.forEach {
            verify {
                spec.or(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        assertEquals(spec, result)
    }

    @Test
    fun shouldBuildOrCriteria() {
        val criteria = listOf(mockCriteria(Type.OR), mockCriteria(Type.OR))
        val spec = mockSpecificationChain()

        val result = specificationBuilder.build<Any>(criteria)
        criteria.forEach {
            verify {
                spec.or(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        assertEquals(spec, result)
    }

    @Test
    fun shouldBuildMixedCriteria() {
        val andCriteria = listOf(mockCriteria(Type.AND), mockCriteria(Type.AND))
        val securityCriteria = listOf(mockCriteria(Type.SECURITY), mockCriteria(Type.SECURITY))
        val orCriteria = listOf(mockCriteria(Type.OR), mockCriteria(Type.OR))

        val spec = mockSpecificationChain()

        val result = specificationBuilder.build<Any>(andCriteria + securityCriteria + orCriteria)

        andCriteria.forEach {
            verify {
                spec.and(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        securityCriteria.forEach {
            verify {
                spec.or(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        orCriteria.forEach {
            verify {
                spec.or(
                    EntitySpecification<Any>(
                        it,
                        PredicateBuilder(typeConverter)
                    )
                )
            }
        }
        assertEquals(spec, result)
    }

    private fun mockSpecificationChain(): Specification<Any> {
        mockkStatic(Specification::class)
        val spec = mockk<Specification<Any>>()
        every { Specification.where<Any>(any()) } returns spec
        every { spec.and(any()) } returns spec
        every { spec.or(any()) } returns spec
        return spec
    }

    private fun mockCriteria(type: Type) =
        mock<SearchCriteria>()
            .also {
                whenever(it.type).thenReturn(type)
            }

}