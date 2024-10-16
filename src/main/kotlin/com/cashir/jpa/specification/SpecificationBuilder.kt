package com.cashir.jpa.specification

import org.springframework.data.jpa.domain.Specification

open class SpecificationBuilder(
    typeConverter: TypeConverter = TypeConverter(),
) {
    private val predicateBuilder: PredicateBuilder = PredicateBuilder(typeConverter)

    fun <T> build(searchCriteriaList: List<SearchCriteria>): Specification<T> {
        val andContext = searchCriteriaList.filter { it.type == SearchCriteria.Type.AND }
        val orContext = searchCriteriaList.filter { it.type == SearchCriteria.Type.OR }
        val securityContext = searchCriteriaList.filter { it.type == SearchCriteria.Type.SECURITY }
        return andContext.buildAndSpecification<T>()
            .and(orContext.buildOrSpecification())
            .and(securityContext.buildOrSpecification<T>())

    }

    private fun <T> List<SearchCriteria>.buildAndSpecification(): Specification<T> {
        var result = Specification.where<T>(null)
        this.forEach { result = Specification.where(result).and(EntitySpecification(it, predicateBuilder)) }
        return result
    }

    private fun <T> List<SearchCriteria>.buildOrSpecification(): Specification<T> {
        var result = Specification.where<T>(null)
        this.forEach { result = Specification.where(result).or(EntitySpecification(it, predicateBuilder)) }
        return result
    }
}