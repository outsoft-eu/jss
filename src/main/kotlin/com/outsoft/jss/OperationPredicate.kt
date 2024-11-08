package com.outsoft.jss

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

internal interface OperationPredicate {
    fun <E> buildPredicate(builder: CriteriaBuilder, root: Root<E>, criteria: com.outsoft.jss.SearchCriteria): Predicate
}