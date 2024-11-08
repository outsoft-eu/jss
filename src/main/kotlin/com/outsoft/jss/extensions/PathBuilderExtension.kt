package com.outsoft.jss.extensions

import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root

object PathBuilderExtension {
    private const val KEY_DELIMITER = "."

    fun <T, E> Root<E>.buildPath(key: String): Path<T> {
        val split = key.split(KEY_DELIMITER)
        return when (split.size) {
            1 -> this.get(key)
            else -> {
                createJoin(this, split)
            }
        }
    }

    private fun <T, E> createJoin(root: Root<E>, split: List<String>): Path<T> {
        var join: Join<*, *> = root.getJoin(split.first())
        for (i in 1 until split.size - 1) {
            join = join.getJoin(split[i])
        }
        return join.get(split.last())
    }

    private fun <E> Root<E>.getJoin(key: String): Join<*, *> =
        this.joins.firstOrNull { it.attribute.name == key } ?: this.join<E, Any>(key, JoinType.LEFT)

    private fun <E> Join<E, *>.getJoin(key: String): Join<*, *> =
        this.joins.firstOrNull { it.attribute.name == key } ?: this.join<Any, Any>(key, JoinType.LEFT)
}
