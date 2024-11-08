package com.outsoft.jss.operations

import com.outsoft.jss.PredicateBuilderUtils.notEqualsWithNullsPredicate
import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class NegationOperationPredicate(
    private val typeConverter: TypeConverter,
) : com.outsoft.jss.OperationPredicate {

    override fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: SearchCriteria): Predicate {
        val path = root.buildPath<String, E>(criteria.key)
        val value = criteria.value?.let { typeConverter.convert(it, path.javaType) }
        return builder.notEqualsWithNullsPredicate(path, value)
    }

}