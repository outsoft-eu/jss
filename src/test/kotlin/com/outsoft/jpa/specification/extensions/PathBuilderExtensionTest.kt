package com.outsoft.jpa.specification.extensions

import com.outsoft.jpa.specification.extensions.PathBuilderExtension.buildPath
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root

private const val ONE_LEVEL_PROPERTY = "key"
private const val THREE_LEVEL_PROPERTY = "key1.key2.key3"
private const val KEY_DELIMITER = "."

class PathBuilderExtensionTest {
    private lateinit var rootMock: Root<Any>
    private lateinit var pathMock: Path<String>

    @BeforeEach
    fun setUp() {
        rootMock = mockk()
        pathMock = mockk()
    }

    @Test
    fun `should build one level path`() {
        every { rootMock.get<String>(ONE_LEVEL_PROPERTY) }.returns(pathMock)
        assertEquals(pathMock, rootMock.buildPath<String, Any>(ONE_LEVEL_PROPERTY))
    }

    @Test
    fun `should build 3 level path with joins`() {
        val keyList = THREE_LEVEL_PROPERTY.split(KEY_DELIMITER)

        val joinMock: Join<Any, Any> = mockk()
        every { rootMock.joins }.returns(emptySet())
        every { joinMock.joins }.returns(emptySet())
        every { rootMock.join<Any, Any>(keyList[0], JoinType.LEFT) }.returns(joinMock)
        every { joinMock.join<Any, Any>(keyList[1], JoinType.LEFT) }.returns(joinMock)

        every { joinMock.get<String>(keyList[2]) }.returns(pathMock)

        assertEquals(pathMock, rootMock.buildPath<String, Any>(THREE_LEVEL_PROPERTY))
    }

    @Test
    fun `should build 3 level path with existing join`() {
        val keyList = THREE_LEVEL_PROPERTY.split(KEY_DELIMITER)

        val joinMock: Join<Any, Any> = mockk()
        val secondJoinMock: Join<Any, Any> = mockk()

        every { rootMock.joins }.returns(setOf(joinMock))
        every { joinMock.joins }.returns(setOf(secondJoinMock))
        every { joinMock.attribute.name }.returns(keyList[0])
        every { secondJoinMock.attribute.name }.returns(keyList[1])

        every { secondJoinMock.get<String>(keyList[2]) }.returns(pathMock)

        assertEquals(pathMock, rootMock.buildPath<String, Any>(THREE_LEVEL_PROPERTY))
    }
}