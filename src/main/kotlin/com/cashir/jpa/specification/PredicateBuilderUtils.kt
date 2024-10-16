package com.cashir.jpa.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate

internal object PredicateBuilderUtils {

    internal fun CriteriaBuilder.notEqualsWithNullsPredicate(
        path: Path<String>,
        value: Any? = null
    ): Predicate {
        val notEqual = this.notEqual(path, value)
        return if (value == null)
            this.withoutNullValues(notEqual, path)
        else
            this.withNullValues(notEqual, path)
    }

    internal fun CriteriaBuilder.withNullValues(
        predicate: Predicate,
        path: Path<String>
    ): Predicate =
        this.or(
            predicate,
            this.isNull(path)
        )

    internal fun CriteriaBuilder.withoutNullValues(
        predicate: Predicate,
        path: Path<String>
    ): Predicate =
        this.or(
            predicate,
            this.isNotNull(path)
        )

    internal fun parseListOfValues(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            value.split(", ", ";")
        }
    }
}