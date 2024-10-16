package com.cashir.jpa.specification.operations

import com.cashir.jpa.specification.CriteriaBadRequestException
import com.cashir.jpa.specification.OperationPredicate
import com.cashir.jpa.specification.SearchCriteria
import com.cashir.jpa.specification.TypeConverter
import com.cashir.jpa.specification.extensions.PathBuilderExtension.buildPath
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class LessThanOperationPredicate(
    private val typeConverter: TypeConverter
) : OperationPredicate {
    override fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: SearchCriteria): Predicate {
        val path: Path<String> = root.buildPath(criteria.key)
        return when (val value = criteria.value?.let { typeConverter.convert(it, path.javaType) }) {
            is String -> builder.lessThanOrEqualPredicate(path, value)
            is Long -> builder.lessThanOrEqualPredicate(path, value)
            is Int -> builder.lessThanOrEqualPredicate(path, value)
            is Boolean -> builder.lessThanOrEqualPredicate(path, value)
            is LocalDateTime -> builder.lessThanOrEqualPredicate(path, value)
            is LocalDate -> builder.lessThanOrEqualPredicate(path, value)
            is BigDecimal -> builder.lessThanOrEqualPredicate(path, value)
            is ZonedDateTime -> builder.lessThanOrEqualPredicate(path, value)
            is Double -> builder.lessThanOrEqualPredicate(path, value)
            else -> throw CriteriaBadRequestException(criteria, root)
        }
    }

    private fun <T : Comparable<T>> CriteriaBuilder.lessThanOrEqualPredicate(
        path: Path<String>,
        value: T
    ): Predicate = this.lessThanOrEqualTo(path.`as`(value.javaClass), value)
}
