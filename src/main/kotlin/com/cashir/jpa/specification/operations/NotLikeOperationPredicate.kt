package com.cashir.jpa.specification.operations

import com.cashir.jpa.specification.OperationPredicate
import com.cashir.jpa.specification.PredicateBuilderUtils.withNullValues
import com.cashir.jpa.specification.SearchCriteria
import com.cashir.jpa.specification.extensions.LikePredicateExtension.ESCAPE_CHARACTER
import com.cashir.jpa.specification.extensions.LikePredicateExtension.LIKE_SYMBOL
import com.cashir.jpa.specification.extensions.LikePredicateExtension.maskLikeValue
import com.cashir.jpa.specification.extensions.PathBuilderExtension.buildPath
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal class NotLikeOperationPredicate : OperationPredicate {
    override fun <E> buildPredicate(
        builder: CriteriaBuilder,
        root: Root<E>,
        criteria: SearchCriteria,
    ): Predicate {
        val path = root.buildPath<String, E>(criteria.key)
        return builder.withNullValues(
            builder.maskedNotLike(path, criteria.value),
            path
        )
    }

    private fun CriteriaBuilder.maskedNotLike(
        path: Path<String>,
        value: String?
    ): Predicate = this.notLike(
        this.lower(path.`as`(String::class.java)),
        "$LIKE_SYMBOL${maskLikeValue(value?.lowercase())}$LIKE_SYMBOL",
        ESCAPE_CHARACTER
    )

}