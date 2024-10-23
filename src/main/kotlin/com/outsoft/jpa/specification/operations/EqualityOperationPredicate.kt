package com.outsoft.jpa.specification.operations

import com.outsoft.jpa.specification.OperationPredicate
import com.outsoft.jpa.specification.SearchCriteria
import com.outsoft.jpa.specification.TypeConverter
import com.outsoft.jpa.specification.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class EqualityOperationPredicate(
    private val typeConverter: TypeConverter
) : OperationPredicate {
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