package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class GreaterThanOperationPredicate(
    private val typeConverter: TypeConverter,
) : com.outsoft.jss.OperationPredicate {
    override fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: SearchCriteria): Predicate {
        val path: Path<String> = root.buildPath(criteria.key)
        return when (val value = criteria.value?.let { typeConverter.convert(it, path.javaType) }) {
            is String -> builder.greaterThanOrEqualPredicate(path, value)
            is Long -> builder.greaterThanOrEqualPredicate(path, value)
            is Int -> builder.greaterThanOrEqualPredicate(path, value)
            is Boolean -> builder.greaterThanOrEqualPredicate(path, value)
            is LocalDateTime -> builder.greaterThanOrEqualPredicate(path, value)
            is LocalDate -> builder.greaterThanOrEqualPredicate(path, value)
            is BigDecimal -> builder.greaterThanOrEqualPredicate(path, value)
            is ZonedDateTime -> builder.greaterThanOrEqualPredicate(path, value)
            is Double -> builder.greaterThanOrEqualPredicate(path, value)
            else -> throw com.outsoft.jss.CriteriaBadRequestException(criteria, root)
        }
    }

    private fun <T : Comparable<T>> CriteriaBuilder.greaterThanOrEqualPredicate(
        path: Path<String>,
        value: T
    ): Predicate = this.greaterThanOrEqualTo(path.`as`(value.javaClass), value)
}
