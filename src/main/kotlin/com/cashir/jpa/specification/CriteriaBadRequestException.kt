package com.cashir.jpa.specification

import javax.persistence.criteria.Root

class CriteriaBadRequestException(
    override val message: String? = null,
    ex: Throwable? = null
) : RuntimeException(message, ex) {

    constructor(criteria: SearchCriteria, root: Root<*>, ex: Throwable? = null) : this(
        createErrorMessage(criteria, root), ex
    )

    private companion object {
        private fun createErrorMessage(criteria: SearchCriteria, root: Root<*>): String =
            "Incorrect search request for ${root.type().javaType}: {key=${criteria.key}, " +
                "operation=${criteria.operation}, value=${criteria.value}}"
    }
}