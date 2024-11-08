package com.outsoft.jss.operations

import com.outsoft.jss.PredicateBuilderUtils.parseListOfValues
import com.outsoft.jss.PredicateBuilderUtils.withNullValues
import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class NotInOperationPredicate(
    private val typeConverter: TypeConverter,
) : com.outsoft.jss.OperationPredicate {
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