package com.outsoft.jpa.specification

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

data class EntitySpecification<E>(
    private val criteria: SearchCriteria,
    private val predicateBuilder: PredicateBuilder
) : Specification<E> {

    override fun toPredicate(
        root: Root<E>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): Predicate = predicateBuilder.buildPredicate(builder, root, criteria)
}