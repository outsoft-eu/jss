package com.outsoft.jpa.specification.extensions

import com.outsoft.jpa.specification.extensions.LikePredicateExtension.maskLikeValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LikePredicateExtensionTest {
    private val expectedValueMap = mapOf(
        "test" to "test",
        "_" to "\\_",
        "test _ test" to "test \\_ test",
        "_ test" to "\\_ test",
        "test _" to "test \\_"
    )

    @Test
    fun `should mask like value`() {
        expectedValueMap.forEach { assertEquals(it.value, maskLikeValue(it.key)) }
    }
}