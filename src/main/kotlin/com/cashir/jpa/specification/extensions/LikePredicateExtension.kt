package com.cashir.jpa.specification.extensions

object LikePredicateExtension {
    internal const val LIKE_SYMBOL = "%"
    internal const val ESCAPE_CHARACTER = '\\'
    private const val LIKE_ANY_SYMBOL_WILDCARD = "_"
    private val LIKE_MASKED_ANY_SYMBOL = ESCAPE_CHARACTER + LIKE_ANY_SYMBOL_WILDCARD

    internal fun maskLikeValue(value: String?) = value?.replace(LIKE_ANY_SYMBOL_WILDCARD, LIKE_MASKED_ANY_SYMBOL)
}