package com.outsoft.jss

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

data class EntitySpecification<E>(
    private val criteria: com.outsoft.jss.SearchCriteria,
    private val predicateBuilder: com.outsoft.jss.PredicateBuilder,
) : Specification<E> {

    override fun toPredicate(
        root: Root<E>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): Predicate = predicateBuilder.buildPredicate(builder, root, criteria)
}