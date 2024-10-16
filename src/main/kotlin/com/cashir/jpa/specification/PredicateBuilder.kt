package com.cashir.jpa.specification

import com.cashir.jpa.specification.operations.EqualityOperationPredicate
import com.cashir.jpa.specification.operations.GreaterThanOperationPredicate
import com.cashir.jpa.specification.operations.InOperationPredicate
import com.cashir.jpa.specification.operations.LessThanOperationPredicate
import com.cashir.jpa.specification.operations.LikeOperationPredicate
import com.cashir.jpa.specification.operations.NegationOperationPredicate
import com.cashir.jpa.specification.operations.NotInOperationPredicate
import com.cashir.jpa.specification.operations.NotLikeOperationPredicate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

data class PredicateBuilder(
    private val typeConverter: TypeConverter
) {

    internal fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: SearchCriteria): Predicate {
        return try {
            operations[criteria.operation]?.buildPredicate(builder, root, criteria)
                ?: unexpectedOperation(criteria, root)
        } catch (ex: RuntimeException) {
            when (ex) {
                is CriteriaBadRequestException -> throw ex
                else -> throw CriteriaBadRequestException(criteria, root, ex)
            }
        }
    }

    private fun unexpectedOperation(criteria: SearchCriteria, root: Root<*>): Predicate {
        throw CriteriaBadRequestException(criteria, root)
    }

    private val operations = mapOf(
        SearchCriteria.Operation.EQUALITY to EqualityOperationPredicate(typeConverter),
        SearchCriteria.Operation.NEGATION to NegationOperationPredicate(typeConverter),
        SearchCriteria.Operation.LIKE to LikeOperationPredicate(),
        SearchCriteria.Operation.NOT_LIKE to NotLikeOperationPredicate(),
        SearchCriteria.Operation.GREATER_THAN to GreaterThanOperationPredicate(typeConverter),
        SearchCriteria.Operation.LESS_THAN to LessThanOperationPredicate(typeConverter),
        SearchCriteria.Operation.IN to InOperationPredicate(typeConverter),
        SearchCriteria.Operation.NOT_IN to NotInOperationPredicate(typeConverter)
    )
}
