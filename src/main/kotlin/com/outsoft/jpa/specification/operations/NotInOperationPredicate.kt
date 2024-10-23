package com.outsoft.jpa.specification.operations

import com.outsoft.jpa.specification.OperationPredicate
import com.outsoft.jpa.specification.PredicateBuilderUtils.parseListOfValues
import com.outsoft.jpa.specification.PredicateBuilderUtils.withNullValues
import com.outsoft.jpa.specification.SearchCriteria
import com.outsoft.jpa.specification.TypeConverter
import com.outsoft.jpa.specification.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class NotInOperationPredicate(
    private val typeConverter: TypeConverter
) : OperationPredicate {
    override fun <E> buildPredicate(
        builder: CriteriaBuilder,
        root: Root<E>,
        criteria: SearchCriteria,
    ): Predicate =
        parseListOfValues(criteria.value).let { list ->
            if (list.isEmpty()) {
                builder.conjunction()
            } else {
                val path = root.buildPath<String, E>(criteria.key)
                val value = list.map { typeConverter.convert(it, path.javaType) }
                builder.withNullValues(builder.not(path.`in`(value)), path)
            }
        }
}