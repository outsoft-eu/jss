package com.outsoft.jss

import com.outsoft.jss.operations.EqualityOperationPredicate
import com.outsoft.jss.operations.GreaterThanOperationPredicate
import com.outsoft.jss.operations.InOperationPredicate
import com.outsoft.jss.operations.LessThanOperationPredicate
import com.outsoft.jss.operations.LikeOperationPredicate
import com.outsoft.jss.operations.NegationOperationPredicate
import com.outsoft.jss.operations.NotInOperationPredicate
import com.outsoft.jss.operations.NotLikeOperationPredicate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

data class PredicateBuilder(
    private val typeConverter: com.outsoft.jss.TypeConverter,
) {

    internal fun <E> buildPredicate(
        builder: CriteriaBuilder,
        root: Root<E>,
        criteria: com.outsoft.jss.SearchCriteria,
    ): Predicate {
        return try {
            operations[criteria.operation]?.buildPredicate(builder, root, criteria)
                ?: unexpectedOperation(criteria, root)
        } catch (ex: RuntimeException) {
            when (ex) {
                is com.outsoft.jss.CriteriaBadRequestException -> throw ex
                else -> throw com.outsoft.jss.CriteriaBadRequestException(criteria, root, ex)
            }
        }
    }

    private fun unexpectedOperation(criteria: com.outsoft.jss.SearchCriteria, root: Root<*>): Predicate {
        throw com.outsoft.jss.CriteriaBadRequestException(criteria, root)
    }

    private val operations = mapOf(
        com.outsoft.jss.SearchCriteria.Operation.EQUALITY to EqualityOperationPredicate(typeConverter),
        com.outsoft.jss.SearchCriteria.Operation.NEGATION to NegationOperationPredicate(typeConverter),
        com.outsoft.jss.SearchCriteria.Operation.LIKE to LikeOperationPredicate(),
        com.outsoft.jss.SearchCriteria.Operation.NOT_LIKE to NotLikeOperationPredicate(),
        com.outsoft.jss.SearchCriteria.Operation.GREATER_THAN to GreaterThanOperationPredicate(typeConverter),
        com.outsoft.jss.SearchCriteria.Operation.LESS_THAN to LessThanOperationPredicate(typeConverter),
        com.outsoft.jss.SearchCriteria.Operation.IN to InOperationPredicate(typeConverter),
        com.outsoft.jss.SearchCriteria.Operation.NOT_IN to NotInOperationPredicate(typeConverter)
    )
}
