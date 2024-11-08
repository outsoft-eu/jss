package com.outsoft.jss.operations

import com.outsoft.jss.SearchCriteria
import com.outsoft.jss.extensions.LikePredicateExtension.ESCAPE_CHARACTER
import com.outsoft.jss.extensions.LikePredicateExtension.LIKE_SYMBOL
import com.outsoft.jss.extensions.LikePredicateExtension.maskLikeValue
import com.outsoft.jss.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class LikeOperationPredicate : com.outsoft.jss.OperationPredicate {
    override fun <E> buildPredicate(
        builder: CriteriaBuilder,
        root: Root<E>,
        criteria: SearchCriteria
    ): Predicate {
        val path = root.buildPath<String, E>(criteria.key)
        return builder.like(
            builder.lower(path.`as`(String::class.java)),
            "$LIKE_SYMBOL${maskLikeValue(criteria.value?.lowercase())}$LIKE_SYMBOL",
            ESCAPE_CHARACTER
        )
    }
}