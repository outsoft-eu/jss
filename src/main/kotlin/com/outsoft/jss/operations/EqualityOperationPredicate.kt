package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.TypeConverter
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class EqualityOperationPredicate(
    private val typeConverter: TypeConverter,
) : com.outsoft.jss.OperationPredicate {
    override fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: SearchCriteria): Predicate {
        val path: Path<String> = root.buildPath(criteria.key)
        val value = criteria.value?.let { typeConverter.convert(it, path.javaType) }
        return if (value == null) {
            builder.isNull(path)
        } else {
            builder.equal(path, value)
        }
    }
}